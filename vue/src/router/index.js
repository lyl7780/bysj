import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Home.vue'
import AdminHomeView from '../views/admin/Home.vue'
import AdminLoginView from '../views/admin/Login'
import AdminIndex from '../views/admin/Index'
import Tabs from '../views/admin/inc/Tabs'
import axios from "@/axios";
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
    children: [
      {
        path: '/login',
        name: 'Login',
        component: () => import( '../views/Login.vue')
      }
    ]
  },
  {
    path: '/admin',
    name: 'AdminHomeView',
    component: AdminHomeView,
    children: [
      {
        path: '/admin/index',
        name: 'AdminIndex',
        component: AdminIndex
      },
      {
        path: '/admin/personalCenter',
        name: 'personalCenter',
        component: ()=> import('@/views/admin/PersonalCenter.vue')
      },
    ]
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: AdminLoginView
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


router.beforeEach((to, from, next) => {
    let hasRoute = store.state.menus.hasRoute
    if(!hasRoute){
        axios.get("/admin/menu/nav",{
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
                            newRouters[1].children.push(route)
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
