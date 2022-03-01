<template>
  <div>
    <el-form :inline="true" class="demo-searchForm">
      <el-form-item label="">
        <el-input v-model="searchForm.name" placeholder="用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDoctorsByName">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="newlyDoctor">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定批量删除吗？" @confirm="deleteHandle(null)" v-if="hasAuth('sys:doctor:del')">
          <el-button type="danger" slot="reference" :disabled="delBtnStatus">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        border
        stripe
        @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          label="头像"
          width="50">
        <template slot-scope="scope">
          <el-avatar size="small" :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column
          prop="id"
          label="用户ID"
          width="120">
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名"
          width="120">
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="officeId"
          label="部门"
          show-overflow-tooltip>
        <template slot-scope="scope" v-for="item in department">
          <span  >{{item.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="description"
          label="介绍"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="status"
          label="状态"
          show-overflow-tooltip>
        <template slot-scope="scope">
          <el-tag type="success" size="small" v-if="scope.row.status === 1">正常</el-tag>
          <el-tag type="danger" size="small" v-else-if="scope.row.status === 0">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="operate"
          label="操作"
          show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button type="text" @click="getHandle(scope.row.id)" v-if="hasAuth('sys:doctor:edit')">编辑</el-button>
          <el-divider direction="vertical" v-if="hasAuth('sys:doctor:edit')"></el-divider>
          <template>
            <el-popconfirm title="确定删除吗？" @confirm="deleteHandle(scope.row.id)" v-if="hasAuth('sys:doctor:del')">
              <el-button slot="reference" type="text">删除</el-button>
            </el-popconfirm>
          </template>
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
        :total="total">
    </el-pagination>
    <el-dialog
        title="医生信息"
        :visible.sync="dialogVisibleDoctor"
        width="600px"
        :before-close="HandlerClose"
    >
      <el-form :model="doctorForm" :rules="doctorFormRules" ref="doctorForm" label-width="100px" class="demo-editForm">
        <el-form-item label="用户名" prop="username" label-width="100px">
          <el-input v-model="doctorForm.username" :disabled="disUsername"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name" label-width="100px">
          <el-input v-model="doctorForm.name"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" label-width="100px">
          <el-input v-model="doctorForm.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="office" label-width="100px">
          <el-select v-model="doctorForm.officeId" placeholder="请选择部门">
            <template v-for="item in department">
              <el-option :label="item.name" :value="item.id"></el-option>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status" label-width="100px">
          <el-radio-group v-model="doctorForm.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="postForm('doctorForm')" v-if="hasAuth('sys:doctor:edit')">立即修改</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Doctor",
  data() {
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
    return {
      disUsername: true,
      dialogVisibleDoctor: false,
      delBtnStatus: true,
      searchForm: {
        name: ''
      },
      doctorForm: {
        id: '',
        avatar: '',
        name: '',
        username: '',
        officeId: '',
        description: '',
        password: '',
        status: '',
        createTime: ''
      },
      doctorFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
        ],
        password: [
          { required: true, validator: pwdCheck, trigger: 'blur' }
        ],
        officeId: [
          { required: true, message: '请选择部门', trigger: 'blur' },
        ],
      },
      department: [],
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
    getDoctors() {
      this.getDepartments()
      this.$axios.get('/admin/sys/doctor/list/' + this.size + '/' + this.currentPage).then(res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    },
    getDoctorsByName() {
      this.$axios.get('/admin/sys/doctor/list/' + this.size + '/' + this.currentPage + '/' + this.searchForm.name).then(res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    },
    getDepartments() {
      this.$axios.get('/admin/sys/doctor/departments').then(res => {
        this.department = res.data.data
      })
    },
    deleteHandle(id) {
      const ids = [];
      if (id) {
        ids.push(id)
      } else {
        this.multipleSelection.forEach(row => {
          ids.push(row.id)
        })
      }

      this.$axios.delete('/admin/sys/doctor/del', ids).then(res => {
        this.$message({
          showClose: true,
          message: '删除成功！',
          type: "success",
        })
        this.getDoctors()
      })
    },
    postForm(formName) {//提交修改
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/admin/sys/doctor/' +(this.doctorForm.id?'update'+this.doctorForm.id:'save'), this.doctorForm).then(res => {
            this.$message({
              showClose: true,
              message: '提交成功！',
              type: "success",
              onClose: () => {
                this.getDoctors()
              }
            })
            this.HandlerClose()
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    newlyDoctor(){
      this.dialogVisibleDoctor = true
      this.disUsername = false
      this.getDepartments()
    },
    getHandle(id) {
      this.$axios.get('/admin/sys/doctor/info/' + id).then(res => {
        this.dialogVisibleDoctor = true
        this.disUsername = true
        this.$nextTick(() => {
          this.doctorForm = res.data.data
        })
      })
    },
    HandlerClose(){
      this.resetForm('doctorForm')
      this.dialogVisibleDoctor = false
    },
  },
  created() {
    this.getDoctors()
  }
}
</script>

<style scoped>
.pagination{
  float: right;
  padding-top: 22px;
}
</style>
