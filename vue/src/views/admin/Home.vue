<template>
  <el-container>
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>海上市第一人民医院后台管理系统</strong>
        <div class="header-avatar">
          <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ userInfo.username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <router-link to="/admin/personalCenter" @click="selectMenu({name: 'personalCenter',title: '个人中心'})">
                <el-dropdown-item>
                  个人中心
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="/" target="_blank">前台</el-link>
        </div>
      </el-header>
      <el-main>
        <Tabs/>
        <div style="margin: 0 15px">
          <router-view/>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideMenu from "@/views/admin/inc/SideMenu";
import Tabs from "@/views/admin/inc/Tabs";
export default {
  name: "Home",
  data(){
    return{
      userInfo: {
        id: "",
        username: "",
        avatar: ""
      }
    }
  },
  components: {
    SideMenu,Tabs
  },
  methods: {
    getUserInfo(){
      this.$axios.get("/admin/userInfo").then(res =>{
        this.userInfo = res.data.data
      })
    },
    logout(){
      this.$axios.post('/admin/logout').then( res =>{
        localStorage.clear()
        sessionStorage.clear()
        this.$store.commit("resetState")
        this.$store.commit("resetStateMenu")
        this.$router.replace('/admin/login')
      })

    },
    selectMenu(item){
      this.$store.commit("addTab",item)
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style scoped>
  .el-container{
    height: 100%;
  }

  .el-header{
    background-color: #17B3A3;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  .el-main {
    color: #333;
    padding: 0;
  }
  .header-avatar{
    float: right;
    width: 150px;
    display: flex;
    justify-content: space-around;
    align-items: center;
  }
  .el-dropdown-link {
    cursor: pointer;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
</style>
