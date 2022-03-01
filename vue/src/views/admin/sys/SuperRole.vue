<template>
  <div>
    <el-form :inline="true" class="demo-searchForm">
      <el-form-item label="">
        <el-input v-model="searchForm.name" placeholder="名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getRolesByName">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="dialogVisible=true">新增</el-button>
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
          prop="name"
          label="名称"
          width="120">
      </el-table-column>
      <el-table-column
          prop="code"
          label="唯一编码"
          show-overflow-tooltip>
      </el-table-column>
      <el-table-column
          prop="description"
          label="描述"
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
          <el-button type="text" @click="getPermHandle(scope.row.id)">分配权限</el-button>
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
        title="角色信息"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="this.HandlerClose"
    >
      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-editForm">
        <el-form-item label="名称" prop="name" label-width="100px">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="唯一编码" prop="code" label-width="100px">
          <el-input v-model="editForm.code"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description" label-width="100px">
          <el-input v-model="editForm.description"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status" label-width="100px">
          <el-radio-group v-model="editForm.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="postForm('editForm')">立即创建</el-button>
          <el-button @click="resetEditForm('editForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
        title="分配权限"
        :visible.sync="dialogVisiblePerm"
        width="600px"
    >
      <el-form :model="permForm">
        <el-tree
            :data="permTreeData"
            show-checkbox
            ref="permTree"
            node-key="id"
            :check-strictly=true
            :default-expand-all=true
            :props="defaultProps">
        </el-tree>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisiblePerm = false">取 消</el-button>
        <el-button type="primary" @click="submitPermForm('permForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SuperRole",
  data() {
    return {
      dialogVisiblePerm: false,
      dialogVisible: false,
      currentPage: 1,
      size: 10,
      total: 0,
      delBtnStatus: true,
      searchForm: {
        name: ''
      },
      permForm: {},
      editForm: {
        id: '',
        name: '',
        code: '',
        description: '',
        status: ''
      },
      editFormRules: {
        name: [
          {required: true, message: '请输入角色名称', trigger: 'blur'},
        ],
        code: [
          {required: true, message: '请输入唯一编码', trigger: 'blur'},
        ],
        status: [
          {required: true, message: '请选择状态', trigger: 'blur'},
        ],
      },
      tableData: [],
      multipleSelection: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      permTreeData: []
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
    resetEditForm(formName) {
      this.$refs[formName].resetFields();
    },
    HandlerClose() {
      this.resetEditForm('editForm')
      this.dialogVisible = false
    },
    getHandle(id) {
      this.$axios.get('/admin/sys/role/info/' + id).then(res => {
        this.dialogVisible = true
        this.$nextTick(() => {
          this.editForm = res.data.data
        })
      })
    },
    postForm(formName) {//创建新数据
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/admin/sys/role/' + (this.editForm.id ? 'update' : 'save'), this.editForm).then(res => {
            this.$message({
              showClose: true,
              message: '创建成功！',
              type: "success",
              onClose: () => {
                this.getRoles()
              }
            })
            this.resetEditForm('editForm')
            this.dialogVisible = false

          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    getRoles() {
      this.$axios.get('/admin/sys/role/list/' + this.size + '/' + this.currentPage).then(res => {
        this.tableData = res.data.data.records
        this.size = res.data.data.size
        this.currentPage = res.data.data.currentPage
        this.total = res.data.data.total
      })
    },
    getRolesByName() {
      this.$axios.get('/admin/sys/role/list/' + this.size + '/' + this.currentPage + '/' + this.searchForm.name).then(res => {
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

      this.$axios.delete('/admin/sys/role/del', ids).then(res => {
        this.$message({
          showClose: true,
          message: '删除成功！',
          type: "success",
        })
        this.getRoles()
      })
    },
    getPermHandle(id) {
      this.dialogVisiblePerm = true
      this.$axios.get('/admin/sys/role/menu/' + id).then(res => {
        this.$refs.permTree.setCheckedKeys(res.data.data.menuIds)
        this.permForm = res.data.data
      })
    },
    submitPermForm(formName) {
      const menuIds = this.$refs.permTree.getCheckedKeys()
      this.$axios.put('/admin/sys/role/perm/' + this.permForm.id, menuIds).then(ref => {
        this.$message({
          showClose: true,
          message: '创建成功！',
          type: "success",
          onClose: () => {
            this.getRoles()
          }
        })
        this.dialogVisiblePerm = false
      })

    }
  },
  created() {
    this.getRoles()
    this.$axios.get('/admin/sys/menu/list').then(res => {
      this.permTreeData = res.data.data
    })
  }
}
</script>

<style scoped>
.pagination {
  float: right;
  margin-top: 22px;
}
</style>
