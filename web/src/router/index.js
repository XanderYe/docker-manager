import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'login',
      meta: {
        title: '登录',
      },
      component: () => import('@/components/Login.vue')
    },
    {
      path: '/',
      name: 'main',
      redirect: '/index',
      component: Main,
      children:[
        {
          path: 'index',
          meta: {
            title: '首页',
          },
          name: 'index',
          component: () => import('@/components/Index.vue')
        }
      ]
    }
  ]
})
