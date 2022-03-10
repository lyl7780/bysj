<template>
  <div>
    <el-table
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe>
      <el-table-column
          prop="orderId"
          label="预约ID"
          width="180">
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名"
          width="180">
      </el-table-column>
      <el-table-column
          prop="sex"
          label="性别"
          width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.sex === 0">女</span>
          <span v-else-if="scope.row.sex === 1">男</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="age"
          label="年龄"
          width="180">
      </el-table-column>
      <el-table-column
          prop="date"
          label="日期">
      </el-table-column>
      <el-table-column
          prop="cov"
          label="是否新冠">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.cov === 0">正常</el-tag>
          <el-tag type="warning" v-else-if="scope.row.cov === 1">疑似</el-tag>
          <el-tag type="danger" v-else-if="scope.row.cov === 2">确诊</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="diagnosisStatus"
          label="诊断状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.diagnosisStatus === 1">已诊断</el-tag>
          <el-tag type="danger" v-else-if="scope.row.diagnosisStatus === 0">未诊断</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="operation"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="getHandle(scope.row.orderId)">诊断</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
        title="诊断信息"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="this.HandlerClose"
    >
      <el-descriptions title="用户信息">
        <el-descriptions-item label="名字">{{ des.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <span v-if="des.sex === 1">男</span>
          <span v-if="des.sex === 0">女</span>
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ des.age }}</el-descriptions-item>
        <el-descriptions-item label="是否新冠">
          <el-tag size="small" type="success" v-if="des.cov === 0">正常</el-tag>
          <el-tag size="small" type="warning" v-if="des.cov === 1">疑似</el-tag>
          <el-tag size="small" type="danger" v-if="des.cov === 2">确诊</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <el-form :model="editForm" ref="editForm" label-width="100px" class="demo-editForm">
        <el-form-item label="诊断内容" prop="diagnosis" label-width="100px">
          <el-input type="textarea" :rows="20" v-model="editForm.diagnosis"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="postForm('editForm')">立即创建</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Order",
  data() {
    return {
      dialogVisible: false,
      editForm: {
        orderId: '',
        diagnosis: ''
      },
      des: {
        name: '',
        sex: '',
        age: '',
        cov: '',
      },
      tableData: []
    }
  },
  methods: {
    getOrders() {
      this.$axios.get("/doctor/sys/orders").then(res => {
        console.log(res.data.data)
        this.tableData = res.data.data
      })
    },
    getHandle(id) {
      this.dialogVisible = true
      console.log(id)
      this.$axios.get("/doctor/sys/orderInfo/"+id).then( res =>{
        this.editForm = res.data.data
        this.des = res.data.data
      })
    },
    postForm(formName){
      console.log(this.editForm.orderId)
      this.$axios.post("/doctor/sys/order",this.editForm).then( res =>{
        this.$message({
          showClose: true,
          message: '创建成功！',
          type: "success",
          onClose:()=>{
            this.getOrders()
          }
        })
        this.resetEditForm('editForm')
        this.dialogVisible = false
      })
    },
    resetEditForm(formName) {
      this.$refs[formName].resetFields();
    },
    HandlerClose() {
      this.dialogVisible = false
    }
  },
  created() {
    this.getOrders()
  }
}
</script>

<style scoped>

</style>
