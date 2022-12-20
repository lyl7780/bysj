<template>
  <div>
    <p class="konGe"></p>
    <h2>坚持真理、坚守理想</h2>
    <h2>践行初心、担当使命</h2>
    <h2>不怕牺牲、英勇斗争</h2>
    <h2>对党忠诚、不负人民</h2>
    <br><br><br><br><br><br><br><br><br>
    <span>今日预计有{{this.order}}名患者前来问诊，目前有{{this.cov}}例疑似患者</span>
  </div>
</template>

<script>

export default {
  name: "index",
  data(){
    return{
      order: '',
      cov: ''
    }
  },
  methods:{
    getNowOrder(){
      this.$axios.get('/admin/sys/orderToday').then( res =>{
        this.order = res.data.data
      })
    },
    getNowCov(){
      this.$axios.get('/admin/sys/covCount').then( res =>{
        this.cov = res.data.data
      })
    }
  },
  created() {
    this.getNowOrder()
    this.getNowCov()
  }
}
</script>

<style scoped>
.konGe{
  padding-top: 150px;
}
*{
  text-align: center;
}
</style>
