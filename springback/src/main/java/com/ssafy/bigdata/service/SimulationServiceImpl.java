package com.ssafy.bigdata.service;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dao.simulation.SimulationDao;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;
import com.ssafy.bigdata.dto.User;
import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;
import com.ssafy.bigdata.dto.simulation.SimulationData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationServiceImpl implements SimulationService {

    @Autowired
    SimulationDao simulationDao;
    @Autowired
    PlayerDao playerDao;
    @Autowired
    UserService userService;

    @Override
    public int createSimulation(int user_id, int my_lineup_id, int your_lineup_id, boolean is_attack, int innings,
            boolean is_top, int out_count, String base_info, String my_score, String your_score, int my_hit_order,
            int your_hit_order, int game_status) {
        int res = simulationDao.createSimulation(user_id, my_lineup_id, your_lineup_id, is_attack, innings, is_top,
                out_count, base_info, my_score, your_score, my_hit_order, your_hit_order, game_status);
        return res;
    }

    @Override
    public Simulation searchSimulation(int simulation_id) {
        Simulation ret = simulationDao.searchSimulation(simulation_id);
        return ret;
    }

    @Override
    public int searchSimulationByUserId(int user_id) {
        int ret = simulationDao.searchSimulationByUserId(user_id);
        return ret;
    }

    @Override
    public HashMap<String, Object> progressSimulation(Simulation simulation, int simulation_id, Score score,
            List<Integer> my_lineup, List<Integer> your_lineup, User user) {
        System.out.println("게임 진행 시작");
        StringBuilder sb = new StringBuilder();
        int[] base_info_array = new int[3];
        StringTokenizer st = new StringTokenizer(simulation.getBase_info(), ",");
        for (int i = 0; i < 3; i++) {
            base_info_array[i] = Integer.parseInt(st.nextToken());
        }

        int my_hit_order = simulation.getMy_hit_order();
        if (my_hit_order == 9) {
            my_hit_order = 0;
            simulation.setMy_hit_order(my_hit_order);
        }
        int your_hit_order = simulation.getYour_hit_order();
        if (your_hit_order == 9) {
            your_hit_order = 0;
            simulation.setMy_hit_order(your_hit_order);
        }
        String my_score = score.getMy_score();
        int[] my_score_arr = new int[12];
        String your_score = score.getYour_score();
        int[] your_score_arr = new int[12];
        int out_count = 0;

        // 점수 정보
        st = new StringTokenizer(simulation.getMy_score(), ",");
        for (int i = 0; i < 12; i++) {
            my_score_arr[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(simulation.getYour_score(), ",");
        for (int i = 0; i < 12; i++) {
            your_score_arr[i] = Integer.parseInt(st.nextToken());
        }

        int strike = 0;
        int ball = 0;
        int hitter_no = 0;
        double hit = 0;
        int hit_order = 0;
        boolean is_attack = simulation.isIs_attack();
        RecordHitter hitter;
        RecordPitcher pitcher;
        double random;
        int roota = 0;
        HitInfo hit_info = null;

        // 공격인 경우
        if (is_attack) {
            hitter_no = my_lineup.get(my_hit_order);
            hit_order = my_hit_order;
            hitter = playerDao.getPlayerStatsHitter(my_lineup.get(my_hit_order));
            pitcher = playerDao.getPlayerStatsPitcher(your_lineup.get(9));

            hit_info = searchHitInfo(simulation_id, my_lineup.get(my_hit_order));
            System.out.println(hit_info);
            if (hit_info == null) {
                createHitInfo(simulation_id, my_lineup.get(my_hit_order)); // 생성
                hit_info = searchHitInfo(simulation_id, my_lineup.get(my_hit_order));
            }

        }
        // 수비인 경우
        else {
            hitter_no = my_lineup.get(your_hit_order);
            hit_order = your_hit_order;
            hitter = playerDao.getPlayerStatsHitter(your_lineup.get(your_hit_order));
            pitcher = playerDao.getPlayerStatsPitcher(my_lineup.get(9));
            hit_info = searchHitInfo(simulation_id, your_lineup.get(your_hit_order));
            if (hit_info == null) {
                createHitInfo(simulation_id, your_lineup.get(your_hit_order)); // 생성
                hit_info = searchHitInfo(simulation_id, your_lineup.get(your_hit_order));
            }
        }
        sb.append((hit_order + 1) + "번 타자 ");
        while (strike != 3 && ball != 4) {
            int hit_count = hit_info.getAt_bat_count();
            hit_info.setAt_bat_count(hit_count + 1);
            hit = hitter.getHitter_ba() * 1000;
            random = Math.random() * 1000;
            double hitter_out = (double) 100 * (hitter.getHitter_pa() - hitter.getHitter_so() - hitter.getHitter_hit())
                    / hitter.getHitter_pa();

            if (hit < random) {
                double ball_random = Math.random() * 100;
                if (ball_random > 40) {
                    strike++;
                } else {
                    ball++;
                }
            } else {
                random = Math.random() * 100;
                System.out.println("hitter_out : " + hitter_out + " / random :" + random);
                if (hitter_out < random) {
                    sb.append("수비에 잡혀 아웃!");
                    break;
                } else {
                    if ((hitter.getHitter_homerun() / hitter.getHitter_pa()) * 100 > random) {
                        roota = 4;
                        System.out.println("홈런@@@@@@@@@@@ (hitter.getHitter_homerun() / hitter.getHitter_pa() : "
                                + (hitter.getHitter_homerun() / hitter.getHitter_pa()));
                        sb.append("홈런!\n");
                        int cnt = hit_info.getHomerun_count();
                        hit_info.setHomerun_count(cnt + 1);
                        break;

                    } else if (hitter.getHitter_slg() * 50 > random) {
                        roota = 3;
                        System.out.println(hit_info.toString());
                        System.out.println("3루타@@@@@@@@");
                        sb.append("3루타!\n");
                        int cnt = hit_info.getHit3_count();
                        hit_info.setHit3_count(cnt + 1);
                        break;
                    } else if (hitter.getHitter_slg() * 100 > random) {
                        roota = 2;
                        System.out.println("2루타@@@@@@@@");
                        sb.append("2루타!\n");
                        int cnt = hit_info.getHit2_count();
                        hit_info.setHit2_count(cnt + 1);
                        break;
                    } else {
                        roota = 1;
                        System.out.println("1루타@@@@@@@@");
                        sb.append("1루타!\n");
                        int cnt = hit_info.getHit1_count();
                        hit_info.setHit1_count(cnt + 1);
                        break;
                    }
                }
            }
        }
        System.out.println(roota);
        if (strike == 3) {
            sb.append("삼진 아웃!\n");
            // 아웃 카운트 증가
            out_count++;

        } else if (ball == 4) {
            sb.append("볼넷");
            int cnt = 5;
            roota = 5;
        }
        System.out.println(sb + "");
        System.out.println("roota : " + roota);
        // 진루
        switch (roota) {
            case 1:
                // 1루타
                for (int i = 2; i == 0; i--) {
                    int a = base_info_array[i + 1];
                    if (a != 0) {
                        if (i != 2) {
                            base_info_array[i + 1] = 1;
                            base_info_array[i] = 0;

                        } else {
                            // 득점
                            if (is_attack) {
                                my_score_arr[simulation.getInnings() - 1] += 1;
                                simulation.setMy_score_array(my_score_arr);
                                base_info_array[3] = 0;
                            } else {
                                your_score_arr[simulation.getInnings() - 1] += 1;
                                simulation.setYour_score_array(your_score_arr);
                                base_info_array[3] = 0;
                            }
                        }
                    }
                }
                base_info_array[0] = 1; // 1루 진출

                break;
            case 2:
                // 2루타
                for (int i = 2; i == 0; i--) {
                    int a = base_info_array[i];
                    if (a != 0) {
                        if (i == 1) {
                            base_info_array[2] = 1;
                        } else {
                            // 득점
                            if (is_attack) {
                                my_score_arr[simulation.getInnings() - 1] += 1;
                                simulation.setMy_score_array(my_score_arr);
                                base_info_array[i] = 0;
                            } else {
                                your_score_arr[simulation.getInnings() - 1] += 1;
                                simulation.setYour_score_array(your_score_arr);
                                base_info_array[i] = 0;
                            }
                        }
                    }
                }
                base_info_array[1] = 1; // 2루 진출

            case 3:
                // 3루타
                for (int i = 2; i == 0; i--) {
                    int a = base_info_array[i];
                    if (a != 0) {
                        // 득점
                        if (is_attack) {
                            my_score_arr[simulation.getInnings() - 1] += 1;
                            simulation.setMy_score_array(my_score_arr);
                            base_info_array[i] = 0;
                        } else {
                            your_score_arr[simulation.getInnings() - 1] += 1;
                            simulation.setYour_score_array(your_score_arr);
                            base_info_array[i] = 0;
                        }
                    }
                }
                base_info_array[2] = 1; // 3루 진출
                break;

            case 4:
                // 홈런
                for (int i = 2; i == 0; i--) {
                    int a = base_info_array[i];
                    if (a != 0) {
                        // 득점
                        if (is_attack) {
                            my_score_arr[simulation.getInnings() - 1] += 1;
                            simulation.setMy_score_array(my_score_arr);
                            base_info_array[i] = 0;
                        } else {
                            your_score_arr[simulation.getInnings() - 1] += 1;
                            simulation.setYour_score_array(your_score_arr);
                            base_info_array[i] = 0;
                        }
                    }
                }
                if (is_attack) {
                    my_score_arr[simulation.getInnings() - 1] += 1;
                } else {
                    your_score_arr[simulation.getInnings() - 1] += 1;
                }
                break;

            case 5:
                // 볼넷
                for (int i = 0; i < 3; i++) {
                    int a = base_info_array[i + 1];
                    if (a != 0) {
                        if (i != 2) {
                            base_info_array[i + 1] = 1;
                            base_info_array[i] = 0;

                        } else {
                            // 득점
                            if (is_attack) {
                                my_score_arr[simulation.getInnings() - 1] += 1;
                                base_info_array[3] = 0;
                            } else {
                                your_score_arr[simulation.getInnings() - 1] += 1;
                                base_info_array[3] = 0;
                            }
                        }
                    }
                }
                base_info_array[0] = 1; // 1루 진출

                break;
        }

        String base_info = "";
        for (int i : base_info_array) {
            base_info += i + ",";
        }
        simulation.setBase_info(base_info);
        simulation.setBase_info_array(base_info_array);

        // 점수,타순 저장
        String score_save = "";
        if (is_attack) {
            for (int i : my_score_arr) {
                score_save += i + ",";
            }
            score.setMy_score(score_save);
            simulation.setMy_hit_order(my_hit_order + 1);
        } else {
            for (int i : your_score_arr) {
                score_save += i + ",";
            }
            score.setYour_score(score_save);
            simulation.setMy_hit_order(your_hit_order + 1);
        }
        simulation.setMy_score_array(my_score_arr);
        simulation.setYour_score_array(your_score_arr);
        updateScore(score);

        // 차례가 끝난경우 이닝 증가.
        if (simulation.getOut_count() == 3 && !simulation.isIs_top()) {
            is_attack = (is_attack ? false : true);
            int c = simulation.getInnings();
            simulation.setIs_attack(is_attack);
            simulation.setInnings(c++);
        }

        // update 시뮬레이션 DB
        simulationDao.updateSimulation(simulation);
        System.out.println(simulation.toString());

        // data
        HashMap<String, Object> data = new HashMap<>();
        data.put("simulation", new SimulationData(simulation, score, hit_info));
        data.put("token", userService.getTokenByEmail(user.getEmail()));

        return data;
    }

    @Override
    public int endSimulation(int simulation_id, User user) {

        StringBuilder sb = new StringBuilder();
        // 시뮬레이션 정보 가지고오기
        Simulation simulation = simulationDao.searchSimulation(simulation_id);
        // 시뮬레이션의 game_status를 false로 세팅
        simulation.setGame_status(false);
        int res = simulationDao.updateSimulation(simulation);
        sb.append("게임 종료");
        Score score = simulationDao.searchScore(simulation_id);

        // data
        HashMap<String, Object> data = new HashMap<>();
        data.put("simulation", new SimulationData(simulation, score, null));
        data.put("token", userService.getTokenByEmail(user.getEmail()));
        return res;
    }

    @Override
    public int createScore(int simulation_id) {
        int res = simulationDao.createScore(simulation_id);
        return res;
    }

    @Override
    public Score searchScore(int simulation_id) {
        Score score = simulationDao.searchScore(simulation_id);
        return score;
    }

    @Override
    public int updateScore(Score score) {
        int res = simulationDao.updateScore(score);
        return res;
    }

    @Override
    public int createHitInfo(int simulation_id, int player_id) {
        int res = simulationDao.createHitInfo(simulation_id, player_id);
        return res;
    }

    @Override
    public HitInfo searchHitInfo(int simulation_id, int player_id) {
        HitInfo hit = simulationDao.searchHitInfo(simulation_id, player_id);
        return hit;
    }

    @Override
    public int updateHitInfo(HitInfo hit_info) {
        int res = simulationDao.updateHitInfo(hit_info);
        return res;
    }

}
