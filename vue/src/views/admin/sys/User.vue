<template>
  <div>
    <el-form :inline="true" class="demo-searchForm">
      <el-form-item label="">
        <el-input v-model="searchForm.name" placeholder="用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getUsers">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="确定批量删除吗？" @confirm="deleteHandle(null)">
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
          prop="phone"
          label="手机号"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="idCard"
          label="身份证号"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="created"
          label="创建时间"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="updated"
          label="修改时间"
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
          <el-button type="text" @click="repassHandle(scope.row.id, scope.row.username)">重置密码</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" @click="getHandle(scope.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定删除吗？" @confirm="deleteHandle(scope.row.id)">
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
        title="修改信息"
        :visible.sync="dialogVisibleUser"
        width="600px"
    >
      <el-form :model="userForm" :rules="userFormRules" ref="userForm" label-width="100px" class="demo-editForm">
        <el-form-item label="用户名" prop="username" label-width="100px">
          <el-input v-model="userForm.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name" label-width="100px">
          <el-input v-model="userForm.name"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="phone" label-width="100px">
          <el-input v-model="userForm.idCard"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone" label-width="100px">
          <el-input v-model="userForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status" label-width="100px">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="putForm('userForm')">立即修改</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
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
      dialogVisibleUser: false,
      delBtnStatus: true,
      searchForm: {
        name: ''
      },
      userForm: {
        id: '',
        avatar: '',
        name: '',
        username: '',
        idCard: '',
        status: '',
        phone: '',
        created: ''
      },
      userFormRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
        ],
        password: [
          { required: true, validator: pwdCheck, trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '请输入身份证号', trigger: 'blur' }
        ],
      },
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
    getUsers() {
      this.$axios.get('/admin/sys/user/list' ,{
        params: {
          name: this.searchForm.name,
          current: this.currentPage,
          size: this.size
        }
      }).then(res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
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

      this.$axios.post('/admin/sys/user/del', ids).then(res => {
        this.$message({
          showClose: true,
          message: '删除成功！',
          type: "success",
        })
        this.getUsers()
      })
    },
    putForm(formName) {//提交修改
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.put('/admin/sys/user', this.userForm).then(res => {
            this.$message({
              showClose: true,
              message: '创建成功！',
              type: "success",
              onClose: () => {
                this.getUsers()
              }
            })
            this.dialogVisibleUser = false

          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    repassHandle(id, username) {

      this.$confirm('将重置用户【' + username + '】的密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post("/admin/resetPassword", id).then(res => {
          this.$message({
            showClose: true,
            message: '恭喜你，操作成功',
            type: 'success',
            onClose: () => {
            }
          });
        })
      })
    },
    getHandle(id) {
      this.$axios.get('/admin/sys/user/info/' + id).then(res => {
        this.dialogVisibleUser = true
        this.$nextTick(() => {
          this.userForm = res.data.data
        })
      })
    },
  },
  created() {
    this.getUsers()
  }
}
</script>

<style scoped>
.pagination{
  float: right;
  padding-top: 22px;
}
</style>
