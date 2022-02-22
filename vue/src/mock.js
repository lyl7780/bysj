const Mock = require("mockjs")
const Random = Mock.Random
let result ={
    code: 200,
    msg: '操作成功',
    data: null
}

Mock.mock('/captcha','get',()=>{

    result.data = {
        token: Random.string(32),
        captchaImg: Random.dataImage('100x40','p7n5w')
    }

    return result
})

Mock.mock('/admin/captcha','get',()=>{

    result.data = {
        token: Random.string(32),
        captchaImg: Random.dataImage('100x40','p7n5w')
    }

    return result
})

Mock.mock('/login','get',()=>{

    result.code = 400
    result.msg = "验证码错误"
    return result
})

Mock.mock('/admin/login','get',()=>{

    result.code = 200
    result.msg = "登录成功"
    return result
})

Mock.mock('/admin/userInfo','get',()=>{

    result.data = {
        id: "12",
        username: "woc",
        avatar: ""
    }
    return result
})


Mock.mock('/admin/logout','post',()=>{
    result.code= 200
        result.msg= '操作成功'
    return result
})

Mock.mock('/admin/menu/nav','get',()=>{
    let nav = [
        {
            name: 'sysManager',
            title: '系统管理',
            icon: 'el-icon-s-operation',
            component: '',
            path: '',
            children: [
                {
                    name: 'sysUser',
                    title: '用户管理',
                    icon: 'el-icon-s-custom',
                    component: 'admin/sys/User',
                    path: '/admin/sys/user',
                    children: []
                },
                {
                    name: 'sysDoctor',
                    title: '医生管理',
                    icon: 'el-icon-user',
                    component: 'admin/sys/Doctor',
                    path: '/admin/sys/doctor',
                    children: []
                },
                {
                    name: 'SuperRole',
                    title: '角色管理',
                    icon: 'el-icon-rank',
                    component: 'admin/sys/SuperRole',
                    path: '/admin/sys/SuperRole',
                    children: []
                },
                {
                    name: 'DoctorRole',
                    title: '部门管理',
                    icon: 'el-icon-s-data',
                    component: 'admin/sys/DoctorRole',
                    path: '/admin/sys/doctorRole',
                    children: []
                },
                {
                    name: 'Menu',
                    title: '菜单管理',
                    icon: 'el-icon-menu',
                    component: 'admin/sys/Menu',
                    path: '/admin/sys/Menu',
                    children: []
                }
            ]
        }
    ]
    let authorities = []
    result.data ={
        nav: nav,
        authorities: authorities
    }
    return result
})