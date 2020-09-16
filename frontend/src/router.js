import Vue from 'vue'
import Router from 'vue-router'
import DashboardLayout from '@/layout/DashboardLayout'
import AuthLayout from '@/layout/AuthLayout'
Vue.use(Router)

export default new Router({
  linkExactActiveClass: 'active',
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: 'main',
      component: DashboardLayout,
      children: [
        {
          path: '/main',
          name: 'main',
          component: () => import(/* webpackChunkName: "demo" */ './views/Main.vue')
        },
        {
          path: '/dashboard',
          name: 'dashboard',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Dashboard.vue')
        },
        {
          path: '/search/player',
          name: 'search_player',
          component: () => import(/* webpackChunkName: "demo" */ './views/SearchPlayer.vue')
        },
        {
          path: '/recommend',
          name: 'recommend',
          component: () => import('./views/RecommendPlayer.vue')
        },
        /*
        {
          path: '/icons',
          name: 'icons',
          component: () => import('./views/Icons.vue')
        },
        {
          path: '/profile',
          name: 'profile',
          component: () => import('./views/UserProfile.vue')
        },
        {
          path: '/maps',
          name: 'maps',
          component: () => import('./views/Maps.vue')
        },
        {
          path: '/tables',
          name: 'tables',
          component: () => import('./views/Tables.vue')
        }
        */
      ]
    },
    {
      path: '/',
      redirect: 'login',
      component: AuthLayout,
      children: [
        {
          path: '/login',
          name: 'login',
          component: () => import(/* webpackChunkName: "demo" */ './views/Login.vue')
        },
        {
          path: '/register',
          name: 'register',
          component: () => import(/* webpackChunkName: "demo" */ './views/Register.vue')
        }
      ]
    }
  ]
})
