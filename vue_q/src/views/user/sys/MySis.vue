<template>
  <div>
    <el-form :inline="true" style="text-align: left">
      <el-form-item>
        <el-date-picker
            v-model="date"
            align="right"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button @click="getTableData">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe
        @selection-change="handleSelectionChange">
      <el-table-column
          prop="orderId"
          label="预约ID"
          width="180">
      </el-table-column>
      <el-table-column
          prop="name"
          label="医生"
          width="180">
      </el-table-column>
      <el-table-column
          prop="department"
          label="部门"
          width="180">
      </el-table-column>
      <el-table-column
          prop="description"
          label="简介"
          width="280">
      </el-table-column>
      <el-table-column
          prop="date"
          label="日期">
      </el-table-column>
      <el-table-column
          prop="operate"
          label="操作"
          show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button type="text" @click="findSis(scope.row.orderId)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[ 10 , 15 , 20 ]"
        :page-size="size"
        class="pagination"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="text-align: right;padding-top: 22px">
    </el-pagination>
    <el-dialog
        title="诊断信息"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="this.HandlerClose"
    >
      <el-descriptions>
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
        <el-descriptions-item label="诊断内容">{{ des.diagnosis }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "MySis",
  data(){
    return{
      dialogVisible: false,
      pickerOptions: {
        disabledDate(time) {
          return false;
        },shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '明天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }]
      },
      des: {
        name: '',
        sex: '',
        age: '',
        diagnosis: '',
      },
      date: '',
      tableData: [],
      currentPage: 1,
      size: 10,
      total: 0
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.delBtnStatus = val.length === 0
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
    getTableData(){
      this.$axios.get('/user/sys/mySis',{
        params:{
          date: this.date
        }
      }).then( res =>{
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    },
    findSis(id){
      this.dialogVisible = true
      this.$axios.get("/user/sys/sisInfo/"+id).then( res =>{
        this.des = res.data.data
      })
    },
    HandlerClose() {
      this.dialogVisible = false
    }
  },
  watch: {
    "date"(val1){
      if(val1 == null){
        this.date = ''
      }
    }

  },
  created() {
    this.getTableData()
  }
}
</script>

<style scoped>

</style>
