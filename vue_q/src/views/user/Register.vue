<template>

    <el-row type="flex" class="row-bg" justify="center">
      <h1 style="line-height: 100px;">注册信息</h1>
      <el-col :xl="6" :lg="7">
        <el-form ref="form" :model="form" :rules="formRule" label-width="80px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" show-password></el-input>
          </el-form-item>
          <el-form-item label="重复密码" prop="repassword">
            <el-input v-model="form.repassword" show-password></el-input>
          </el-form-item>
          <el-form-item label="名字" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="code" style="width: 380px;height: 40px">
            <el-input v-model="form.code" style="width: 172px; float: left"></el-input>
            <el-image :src="captchaImg" class="captchaImg" @click="getCaptcha()"></el-image>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">立即创建</el-button>
            <el-button @click="login">返回</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
</template>

<script>
export default {
  name: "Register",
  data() {
    // 密码验证
    const pwdCheck = async(rule, value, callback) => {
      let reg = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}$/
      if(value.length < 1){
        callback(new Error('密码不能为空！'));
      } else if (value.length < 6) {
        callback(new Error('密码不能少于6位！'));
      } else if (value.length > 16) {
        callback(new Error('密码最长不能超过16位！'));
      } else if (!reg.test(value)) {
        callback(new Error('密码必须包含大小写字母，数字以及符号！'));
      } else {
        callback()
      }
    }
    // 重复密码验证
    const pwdAgainCheck = async(rule, value, callback) => {
      if (value.length < 1) {
        callback(new Error('重复密码不能为空！'));
      } else if(value !== this.form.password){
        callback(new Error('两次输入密码不一致！'));
      }else{
        callback()
      }
    };
    return {
      form: {
        username: '',
        password: '',
        name: '',
        repassword: '',
        idCard: '',
        phone: '',
        code: '',
        token: '',
      },
      formRule: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, validator: pwdCheck, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        repassword: [
          { required: true, validator: pwdAgainCheck, trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '请输入 18 位身份证号', trigger: 'blur' },
          { min: 18, max: 18, message: '长度为 18 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度为 11 个字符', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 5, max: 5, message: '长度为 5 个字符', trigger: 'blur' }
        ],
      },
      captchaImg: ''
    }
  },
  methods: {
    onSubmit() {
      this.$confirm('注册后您将无法修改个人信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post("/register",this.form).then(res => {
          this.$message({
            type: 'success',
            message: '注册成功!'
          });
          this.$router.push("/login").catch(err =>{})
        })

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消注册'
        });
      });
    },
    login(){
      this.$router.push("/login").catch(err =>{})
    },
    getCaptcha(){
      this.$axios.get('/captcha').then(res =>{
        this.form.token = res.data.data.token
        this.captchaImg = res.data.data.captchaImg
        this.form.code = ''
      })
    }
  },
  created() {
    this.getCaptcha()
  }
}
</script>

<style scoped>
html{
  background-color: #fafafa;
}
.el-row{

  height: 100%;
  display: flex;
  align-items: center;
  text-align: center;
  flex-direction: column;
  margin-top: -90px;
  margin-left: -60px;
}
.captchaImg{
  float: left;
  margin-left: 8px;
  border-radius: 4px;
  cursor: pointer;
}

</style>
