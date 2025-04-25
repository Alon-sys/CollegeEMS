import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: '首页',
    component: () => import('../views/HomeView.vue'),
    meta: {
      title: '首页'
    },
    children:[
   
      
    ]
  },
  {
    path:"/register",
    name:"注册界面",
    component: () => import('../views/RegisterView.vue'),
    meta: {
      title: '注册界面'
    }
  },
  {
    path: '/login',
    name: '登录界面',
    component: () => import('../views/LoginView.vue'), 
    meta: {
      title: '登录界面'
    }
  },
  {
    path: '/manage',
    name: '后台管理',
    component: () => import('../views/ManageView.vue'),
    
    meta: {
      title: '后台管理',
      requireAuth: true // 需要登录才能访问
    },
    children:[
      {
        path:'/admin',
        name:'管理员界面',
        component:()=>import('../views/AdminView.vue')
      },
      {
        path:'/company',
        name:'企业信息界面',
        component:()=>import('../views/CompanyView.vue'),
        meta:{
          title:'企业界面'
        }
      },
      {
        path:'/student',
        name:'学生界面',
        component:()=>import('../views/StudentView.vue'),
        meta:{
          title:'学生信息界面'
        }
      },
      {
        path:'/recruit',
        name:'招聘信息界面',
        component:()=>import('../views/RecruitView.vue'),
        meta:{
          title:'招聘信息界面'
        }
      },
      {
        path:'/resume',
        name:'简历信息界面',
        component:()=>import('../views/ResumeView.vue'),
        meta:{
          title:'简历信息界面'
        }
      }
    
    
    
    ]
},

{
  path:'/personal',
  name:'个人中心',
  component:()=>import('../views/PersonalView.vue'),
  meta:{
    title:'个人中心'
  }
},
  
 

 
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to ,from, next) => {
  if (to.path ==='/login' || to.path === '/register') {
    next();
  }
  const user = localStorage.getItem("user");
  if (!user && to.path !== '/login'&& to.path !== '/register') {
    return next("/login");
  }
  next();
})

export default router