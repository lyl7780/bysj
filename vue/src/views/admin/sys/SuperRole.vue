<template>
  <el-form :inline="true" :model="formInline" class="demo-form-inline">
    <el-form-item label="">
      <el-input v-model="formInline.user" placeholder="名称"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="onSubmit">搜索</el-button>
      <el-button type="primary" @click="onSubmit">新增</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "SuperRole",
  data() {
    return {
      formInline: {
        user: '',
        region: ''
      }
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!');
    }
  }
}
</script>

<style scoped>

</style>