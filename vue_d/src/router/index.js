import Vue from 'vue'
import VueRouter from 'vue-router'
import DoctorHomeView from '../views/doctor/Home.vue'
import DoctorLoginView from '../views/doctor/Login'
import DoctorIndex from '../views/doctor/Index'
import Tabs from '../views/doctor/inc/Tabs'
import axios from "../axios";
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'DoctorHomeView',
    component: DoctorHomeView,
    children: [
      {
        path: '/index',
        name: 'DoctorIndex',
        meta: {
          title: "首页"
        },
        component: DoctorIndex
      },
      {
        path: '/personalCenter',
        name: 'personalCenter',
        meta:{
          title: "个人中心"
        },
        component: ()=> import('@/views/doctor/PersonalCenter.vue')
      },
    ]
  },
  {
    path: '/login',
    name: 'DoctorLogin',
    component: DoctorLoginView
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


router.beforeEach((to, from, next) => {
  let hasRoute = store.state.menus.hasRoute
  let token = localStorage.getItem("token")
  if (to.path === '/login') {
    next()
  } else if (!token) {
    next({path: '/login'})
  }else if(!hasRoute && token){
    axios.get("/doctor/menu/nav",{
      headers: {
        Authorization: localStorage.getItem("token")
      }
    }).then( res =>{
      //拿到menuList
      store.commit("setMenuList",res.data.data.nav)
      //拿到用户权限
      store.commit("setPermList",res.data.data.authorities)
      //动态绑定路由
      let newRouters = router.options.routes
      res.data.data.nav.forEach( menu =>{
        if(menu.children){
          menu.children.forEach(e =>{
            //转换成路由
            let route = menuToRoute(e)
            //把路由添加到路由管理中
            if(route){
              newRouters[0].children.push(route)
            }
          })
        }
      })
      //路由绑定
      router.addRoutes(newRouters)
      hasRoute = true
      store.commit("changeRouteStatus",hasRoute)
    })
  }
  next()
})
//导航转成路由
const menuToRoute = (menu) =>{
  if(!menu.component){return null}
  let route = {
    name: menu.name,
    path: menu.path,
    meta: {
      icon: menu.icon,
      title: menu.title
    }
  }
  route.component = () => import( '../views/'+menu.component+'.vue')
  return route
}

export default router
