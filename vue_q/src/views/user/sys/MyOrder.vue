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
          prop="registerStatus"
          label="是否签到">
        <template slot-scope="scope">
        <el-tag type="danger" v-if="scope.row.registerStatus === 0">未签到</el-tag>
        <el-tag type="success" v-else-if="scope.row.registerStatus === 1">已签到</el-tag>
      </template>
      </el-table-column>
      <el-table-column
          prop="operate"
          label="操作"
          show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button type="text" v-if="scope.row.registerStatus === 0 && (scope.row.date === new Date() || scope.row.date === '9999-09-09')" @click="openHandler(scope.row.orderId)">签到</el-button>
          <el-button type="text" v-if="scope.row.registerStatus === 1" disabled>已签到</el-button>
          <el-divider v-if="scope.row.registerStatus === 1" direction="vertical"></el-divider>
          <el-button type="text" v-if="scope.row.registerStatus === 1" @click="getPaidui(scope.row.orderId)">状态</el-button>
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
          <el-button type="primary" @click="postForm('editForm')">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
        title="排队信息"
        :visible.sync="dialogVisiblepaidui"
        width="600px"
        :before-close="this.paiduiClose"
    >
      <span>您的排队号是【{{paidui.me}}】号,当前正在诊断的是【{{paidui.now}}】号</span>
    </el-dialog>
  </div>
</template>

<script>
import {toDate} from "_element-ui@2.15.6@element-ui/src/utils/date-util";

export default {
  name: "MyOrder",
  data(){
    return{
      dialogVisible: false,
      dialogVisiblepaidui: false,
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
      paidui: {
        now: '',
        me: ''
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
    openHandler(id){
      this.dialogVisible = true
      this.editForm.id = id
    },
    getTableData(){
      this.$axios.get("/user/sys/order/my",{
        params: {
          date: this.date
        }
      }).then( res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    },
    paiduiClose(){
      this.dialogVisiblepaidui = false
    },
    getPaidui(orderId){
      this.dialogVisiblepaidui = true
      this.$axios.get('/user/sys/register/paidui/'+orderId).then(res =>{
        this.paidui = res.data.data
      })
    },
    HandlerClose(){
      this.dialogVisible = false
    },
    postForm(formName){
      let cov = 0;
      if(this.editForm.cov === 1){
        cov = 2;
      }else if(this.editForm.area === 1 || this.editForm.kes === 1 || this.editForm.meetk === 1){
        cov = 1;
      }
      console.log(this.editForm.id)
      if(cov === 0){
        this.$axios.post("/user/sys/doRegister/"+this.editForm.id+'/'+cov).then( res =>{
          this.$message({
            showClose: true,
            message: '签到成功！',
            type: "success",
          })
          this.HandlerClose()
          this.getTableData()
        })
      }else{
        this.$axios.post("/user/sys/emergencyCheck/"+cov).then( res =>{
          this.HandlerClose()
          this.$alert("请听从相关人员指示，进入发热门诊就诊！","警告");
          this.getTableData()
        })
      }
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
