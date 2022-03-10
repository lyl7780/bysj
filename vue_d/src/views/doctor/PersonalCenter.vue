<template>
  <div style="text-align: center">
    <h2>修改密码</h2><br>
    <el-form :model="rstpwdForm" :rules="rules" ref="rstpwdForm" label-width="100px" class="rstpwdForm" >
      <el-form-item label="旧密码" prop="oldPassword" style="width: 380px;">
        <el-input v-model="rstpwdForm.oldPassword" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword" style="width: 380px;">
        <el-input v-model="rstpwdForm.newPassword" show-password></el-input>
      </el-form-item>
      <el-form-item label="重复密码" prop="repeatPassword" style="width: 380px;height: 40px">
        <el-input v-model="rstpwdForm.repeatPassword" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('rstpwdForm')">修改密码</el-button>
        <el-button @click="resetForm('rstpwdForm')">清空</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "PersonalCenter",
  data(){
    // 密码验证
    const pwdCheck = async(rule, value, callback) => {
      let reg = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}$/
      if(value.length < 1){
        callback(new Error('新密码不能为空！'));
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
      } else if(value !== this.rstpwdForm.newPassword){
        callback(new Error('两次输入密码不一致！'));
      }else{
        callback()
      }
    };
    return{
      rstpwdForm:{
        oldPassword: '',
        newPassword: '',
        repeatPassword: ''
      },
      rules:{
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, validator: pwdCheck, trigger: 'blur' }
        ],
        repeatPassword: [
          { required: true, validator: pwdAgainCheck, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if(valid){
          const _this = this
          this.$axios.post('/doctor/resetPassword',this.rstpwdForm).then( res =>{
            _this.$alert(res.data.msg,'提示',{
              confirmButtonText:'确定',
              callback:action => {
                this.logout()
              }
            });
          })
        }else{
          console.log('error submit!!');
          return false;
        }
      });
    },
    logout(){
      this.$axios.post('/logout').then( res =>{
        localStorage.clear()
        sessionStorage.clear()
        this.$store.commit("resetState")
        this.$store.commit("resetStateMenu")
        this.$router.replace('/login')
      })

    },
    resetForm(formName){
      this.$refs[formName].resetFields();
    }
  },
  created() {
  }
}
</script>

<style scoped>
.rstpwdForm{
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: -80px;
}
</style>
