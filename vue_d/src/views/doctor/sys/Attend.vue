<template>
  <div>
    <el-form :inline="true">
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
      <el-form-item>
        <el-button type="primary" @click="dialogVisible = true">新增</el-button>
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
          prop="attendId"
          label="出诊ID"
          width="180">
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名"
          width="180">
      </el-table-column>
      <el-table-column
          prop="department"
          label="部门"
          width="180">
      </el-table-column>
      <el-table-column
          prop="date"
          label="日期">
      </el-table-column>
      <el-table-column
          prop="number"
          label="剩余可预约数">
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
        :total="total">
    </el-pagination>
    <el-dialog
        title="修改信息"
        :visible.sync="dialogVisible"
        width="600px"
    >
      <el-form :model="doctorForm" :rules="doctorFormRules" ref="doctorForm" label-width="100px" class="demo-editForm">
        <el-form-item label="日期" prop="date" label-width="100px">
          <el-date-picker
              v-model="doctorForm.date"
              align="right"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              :picker-options="updateOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="接诊数" prop="number" label-width="100px">
          <el-input v-model="doctorForm.number"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="postForm('doctorForm')">立即创建</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Attend",
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
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      updateOptions: {
        disabledDate(time) {
          return time.getTime() <= Date.now();
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
      doctorFormRules: {
        date: [
          { required: true, message: '请选择日期', trigger: 'blur' },
        ],
        number: [
          { required: true, message: '请输入数字', trigger: 'blur' }
        ],
      },
      doctorForm: {
        date: '',
        number: ''
      },
      date: '',
      tableData: [],
      currentPage: 1,
      size: 10,
      total: 0
    }
  },
  methods:{
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
      console.log(this.date)
      this.$axios.get("/doctor/attend/list",{
        params: {
          date: this.date,
          current: this.currentPage,
          size: this.size
        }
      }).then(res =>{
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    },
    postForm(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/doctor/sys/attend', this.doctorForm).then(res => {
            this.$message({
              showClose: true,
              message: '创建成功！',
              type: "success",
              onClose: () => {
                this.resetEditForm('doctorForm')
                this.getTableData()
              }
            })
            this.dialogVisible = false

          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetEditForm(formName) {
      this.$refs[formName].resetFields();
    },

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
.pagination{
  float: right;
  padding-top: 22px;
}
</style>
