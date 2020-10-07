package com.ssafy.bigdata.service;

import java.util.List;
import java.util.StringTokenizer;

import com.ssafy.bigdata.dao.player.PlayerDao;
import com.ssafy.bigdata.dao.simulation.SimulationDao;
import com.ssafy.bigdata.dto.RecordHitter;
import com.ssafy.bigdata.dto.RecordPitcher;
import com.ssafy.bigdata.dto.simulation.HitInfo;
import com.ssafy.bigdata.dto.simulation.Score;
import com.ssafy.bigdata.dto.simulation.Simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationServiceImpl implements SimulationService {

    @Autowired
    SimulationDao simulationDao;
    PlayerDao playerDao;

    @Override
    public int createSimulation(int user_id, int my_lineup_id, int your_lineup_id, int is_attack, int innings,
            boolean is_top, int out_count, String base_info, String my_score, String your_score, int hit_order) {
        return 0;
    }

    @Override
    public Simulation searchSimulation(int simulation_id) {
        return null;
    }

    @Override
    public int searchSimulationByUserId(int user_id) {
        return 0;
    }

    @Override
    public Simulation progressSimulation(Simulation simulation, int simulation_id, Score score, List<Integer> my_lineup,
            List<Integer> your_lineup) {

        StringBuilder sb = new StringBuilder();
        int[] base_info_array = new int[3];
        StringTokenizer st = new StringTokenizer(simulation.getBase_info(), ",");
        for (int i = 0; i < simulation.getBase_info().length(); i++) {
            base_info_array[i] = Integer.parseInt(st.nextToken());
        }

        int my_hit_order = simulation.getMy_hit_order();
        int your_hit_order = simulation.getYour_hit_order();
        String my_score = score.getMy_score();
        int[] my_score_arr = new int[my_score.length()];
        String your_score = score.getYour_score();
        int[] your_score_arr = new int[your_score.length()];
        int out_count = 0;
        st = new StringTokenizer(simulation.getBase_info(), ",");

        // 점수 정보
        for (int i = 0; i < my_score.length(); i++) {
            base_info_array[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(simulation.getBase_info(), ",");
        for (int i = 0; i < your_score.length(); i++) {
            base_info_array[i] = Integer.parseInt(st.nextToken());
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
            if (hit_info == null) {
                createHitInfo(simulation_id, my_lineup.get(my_hit_order)); // 생성
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
            }
        }

        while (strike != 3 && ball != 4) {
            sb.append((hit_order+1) + "번 타자 ");
            hit = hitter.getHitter_ba() * 1000;
            random = Math.random() * 1000;
            double hitter_out = (hitter.getHitter_pa() - hitter.getHitter_so() - hitter.getHitter_hit())
                    / hitter.getHitter_pa() * 1000;
            if (hit < random) {
                double ball_random = Math.random() * 100;
                if (ball_random > 40) {
                    strike++;
                } else {
                    ball++;
                }
            } else {
                random = Math.random() * 1000;
                if (hitter_out < random) {
                    sb.append("수비에 잡혀 아웃!");
                } else {
                    if (hitter.getHitter_homerun() / hitter.getHitter_pa() * 100 > random) {
                        roota = 4;
                        sb.append("홈런!\n");
                        hit_info.setHomerun_count(hit_info.getHomerun_count() + 1);

                    } else if (hitter.getHitter_slg() * 50 > random) {
                        roota = 3;
                        sb.append("3루타!\n");
                        int cnt = hit_info.getHit3_count();
                        hit_info.setHit3_count(cnt++);
                    } else if (hitter.getHitter_slg() * 100 > random) {
                        roota = 2;
                        sb.append("2루타!\n");
                        int cnt = hit_info.getHit2_count();
                        hit_info.setHit2_count(cnt++);
                    } else {
                        roota = 1;
                        sb.append("1루타!\n");
                        int cnt = hit_info.getHit1_count();
                        hit_info.setHit1_count(cnt++);
                    }
                }
            }
        }

        if (strike == 3) {
            sb.append("삼진 아웃!\n");
            // 아웃 카운트 증가
            out_count++;

        } else if (ball == 4) {
            sb.append(" 볼넷");
            int cnt = 5;
        }

        // 진루
        switch (roota) {
            case 1:
                // 1루타
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
            case 2:
                // 2루타
                for (int i = 0; i < 3; i++) {
                    int a = base_info_array[i];
                    if (a != 0) {
                        if (i == 1) {
                            base_info_array[2] = 1;
                        } else {
                            // 득점
                            if (is_attack) {
                                my_score_arr[simulation.getInnings() - 1] += 1;
                                base_info_array[i] = 0;
                            } else {
                                your_score_arr[simulation.getInnings() - 1] += 1;
                                base_info_array[i] = 0;
                            }
                        }
                    }
                }
                base_info_array[0] = 1; // 2루 진출

            case 3:
                // 3루타
                for (int i = 0; i < 3; i++) {
                    int a = base_info_array[i];
                    if (a != 0) {
                        // 득점
                        if (is_attack) {
                            my_score_arr[simulation.getInnings() - 1] += 1;
                            base_info_array[i] = 0;
                        } else {
                            your_score_arr[simulation.getInnings() - 1] += 1;
                            base_info_array[i] = 0;
                        }
                    }
                }
                base_info_array[2] = 1; // 3루 진출
                break;

            case 4:
                // 홈런
                for (int i = 0; i < 3; i++) {
                    int a = base_info_array[i];
                    if (a != 0) {
                        // 득점
                        if (is_attack) {
                            my_score_arr[simulation.getInnings() - 1] += 1;
                            base_info_array[i] = 0;
                        } else {
                            your_score_arr[simulation.getInnings() - 1] += 1;
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
        }

        // 점수 저장
        String score_save = "";
        for (int i : my_score_arr) {
            score_save += i + ",";
        }
        if (is_attack) {
            score.setMy_score(score_save);
        } else {
            score.setYour_score(score_save);
        }
        updateScore(score);

        // 홈팀의 차례가 끝난경우 이닝 증가.
        if (simulation.getOut_count() == 3 && !simulation.isIs_top()) {
            is_attack = (is_attack ? false : true);
            int c = simulation.getInnings();
            simulation.setInnings(c++);
        }

        // update 시뮬레이션 DB
        simulationDao.updateSimulation(simulation);

        return simulation;
    }

    @Override
    public int endSimulation(int simulation_id) {
        StringBuilder sb = new StringBuilder();
        // 시뮬레이션 정보 가지고오기
        Simulation simulation = simulationDao.searchSimulation(simulation_id);
        // 시뮬레이션의 game_status를 false로 세팅
        simulation.setGame_status(false);
        int res = simulationDao.updateSimulation(simulation);
        sb.append("게임 종료");
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
        return null;
    }

    @Override
    public int updateHitInfo(HitInfo hit_info) {
        int res = simulationDao.updateHitInfo(hit_info);
        return res;
    }

}
