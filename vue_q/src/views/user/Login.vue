<template>
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :xl="6" :lg="7">
      <h2>海上市第一人民医院预约系统</h2>
      <el-image :src="require('@/assets/logo.png')" style="height: 220px;width: 220px"/>
      <p>一切为了人民，一切服务人民</p>
    </el-col>
    <el-col :span="1">
      <el-divider direction="vertical"></el-divider>
    </el-col>
    <el-col :xl="6" :lg="7">
      <el-form :model="doctorLoginForm" :rules="rules" ref="doctorLoginForm" label-width="100px" class="demo-adminLoginForm">
        <el-form-item label="用户名" prop="username" style="width: 380px;">
          <el-input v-model="doctorLoginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" style="width: 380px;">
          <el-input v-model="doctorLoginForm.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" style="width: 380px;height: 40px">
          <el-input v-model="doctorLoginForm.code" style="width: 172px; float: left"></el-input>
          <el-image :src="captchaImg" class="captchaImg" @click="getCaptcha()"></el-image>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('doctorLoginForm')">登录</el-button>
          <el-button @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>

import qs from "qs";

export default {
  name: "Login",
  data() {
    return {
      doctorLoginForm: {
        username: '',
        password: '',
        code: '',
        token: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 5, max: 5, message: '长度为 5 个字符', trigger: 'blur' }
        ],
      },
      captchaImg:''
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/login?'+qs.stringify(this.doctorLoginForm)).then(res =>{
            const jwt = res.headers['authorization']
            this.$store.commit('SET_TOKEN',jwt)
            this.$router.push("/index").catch(err =>{})
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    register(){
      this.$router.push("/register").catch(err =>{})
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    getCaptcha(){
      this.$axios.get('/captcha').then(res =>{
        this.doctorLoginForm.token = res.data.data.token
        this.captchaImg = res.data.data.captchaImg
        this.doctorLoginForm.code = ''
      })
    }
  },

  created(){
    this.getCaptcha()
  }
}


</script>

<style scoped>
.el-row{
  background-color: #fafafa;
  height: 100%;
  display: flex;
  align-items: center;
  text-align: center;
}
.el-divider{
  height: 200px;
}
.captchaImg{
  float: left;
  margin-left: 8px;
  border-radius: 4px;
  cursor: pointer;
}
</style>
