<template>
  <div>
    <el-form :inline="true" style="text-align: left">
      <el-form-item>
        <el-date-picker
            v-model="doForm.date"
            align="right"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-select v-model="doForm.officeId" placeholder="请选择部门">
          <template v-for="item in department">
            <el-option :label="item.name" :value="item.officeId"></el-option>
          </template>
        </el-select>
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
          prop="description"
          label="简介"
          width="280">
      </el-table-column>
      <el-table-column
          prop="date"
          label="日期">
        <template slot-scope="scope">
          <span v-if="scope.row.date === '9999-09-09'">不限</span>
          <span v-else>{{scope.row.date}}</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="number"
          label="剩余可预约数">
      </el-table-column>
      <el-table-column
          prop="operate"
          label="操作"
          show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button type="text" @click="orderCheck(scope.row.attendId)">预约</el-button>
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
        title="流行病学调查"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="this.HandlerClose"
    >
      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" label-position="left" class="demo-editForm">
        <el-form-item label="1、14天内是否去过中高风险地区" prop="area" label-width="300px">

          <el-radio-group v-model="editForm.area">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="2、您最近是否有发热或咳嗽乏力等症状" prop="kes" label-width="300px">
          <el-radio-group v-model="editForm.kes">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="3、14天内是否接触过发热咳嗽人员" prop="meetk" label-width="300px">
          <el-radio-group v-model="editForm.meetk">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="4、是否被确认为新冠" prop="cov" label-width="300px">
          <el-radio-group v-model="editForm.cov">
            <el-radio :label="0">否</el-radio>
            <el-radio :label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="doOrder()">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DoOrder",
  data(){
    return{
      dialogVisible: false,
      editForm: {
        id: '',
        area: '',
        kes: '',
        meetk: '',
        cov: ''
      },
      editFormRules: {
        area: [
          { required: true, message: '请选择', trigger: 'blur' }
        ],
        kes: [
          { required: true, message: '请选择', trigger: 'blur' }
        ],
        meetk: [
          { required: true, message: '请选择', trigger: 'blur' }
        ],
        cov: [
          { required: true, message: '请选择', trigger: 'blur' }
        ],
      },
      pickerOptions: {
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
      doForm:{
        date: '',
        officeId: '',
      },
      department: [],
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
    HandlerClose(){
      this.dialogVisible = false
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    },
    orderCheck(id){
      this.$axios.get('/user/userInfo').then( res =>{
        if(res.data.data.cov !== 0){
          this.$message({
            type: 'error',
            message: '您当前不能进行预约!'
          });
        }else{
          this.dialogVisible = true
          this.editForm.id = id
        }
      })
    },
    doOrder(){
      let cov = 0;
      if(this.editForm.cov === 1){
        cov = 2;
      }else if(this.editForm.area === 1 || this.editForm.kes === 1 || this.editForm.meetk === 1){
        cov = 1;
      }
      console.log(this.editForm.id)
      if(cov === 0){
        this.$axios.post('/user/sys/order/'+this.editForm.id).then( res =>{
          this.$message({
            type: 'success',
            message: '预约成功!'
          });
          this.HandlerClose()
          this.getTableData()
        })
      }else{
        this.$axios.post("/user/sys/emergencyOrder/"+cov).then( res =>{
          this.HandlerClose()
          this.$alert("我们会立刻电话联系您，请保持讯号畅通，避免外界接触！","警告");
          this.getTableData()
        })
      }
    },
    getDepartments() {
      this.$axios.get('/user/sys/doctor/departments').then(res => {
        this.department = res.data.data
      })
    },
    getTableData(){
      this.$axios.get('/user/sys/doctor/attend',{
        params: {
          date: this.doForm.date,
          officeId: this.doForm.officeId===''?0:this.doForm.officeId,
        }
      }).then( res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    }
  },
  created() {
    this.getDepartments()
    this.getTableData()
  }
}
</script>

<style scoped>

</style>
