<template>
  <div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item>
        <el-button type="primary" @click="newHandler">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
        :data="tableData"
        style="width: 100%;margin-bottom: 20px;"
        row-key="id"
        border
        stripe
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column
          prop="officeId"
          label="部门ID"
          sortable
          width="180">
      </el-table-column>
      <el-table-column
          prop="name"
          label="部门名"
          sortable
          width="780">
      </el-table-column>
      <el-table-column
          prop="operate"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="getHandle(scope.row.officeId,scope.row.name)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定删除吗？" @confirm="deleteHandle(scope.row.officeId)">
              <el-button slot="reference" type="text">删除</el-button>
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
        title="部门信息"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="this.HandlerClose"
    >
      <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px" class="demo-editForm">
        <el-form-item label="菜单名称" prop="name" label-width="100px">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveDepartment('editForm')">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DoctorRole",
  data() {
    return {
      dialogVisible: false,
      formInline: {
        user: ''
      },
      editForm: {
        officeId: '',
        name: ''
      },
      editFormRules: {
        name: [
          {required: true, message: '请输入部门名称', trigger: 'blur'},
        ],
      },
      tableData: []
    }
  },
  methods: {
    getDepartments() {
      this.$axios.get('/admin/sys/doctor/departments').then(res => {
        this.tableData = res.data.data
      })
    },
    getHandle(id, name) {
      this.dialogVisible = true
      this.editForm.officeId = id
      this.editForm.name = name
      console.log(this.editForm.id)
    },
    newHandler() {
      this.dialogVisible = true
      this.editForm.officeId = ''
      this.editForm.name = ''
    },
    HandlerClose() {
      this.editForm.officeId = ''
      this.editForm.name = ''
      this.dialogVisible = false
    },
    saveDepartment() {
      this.$axios.post('/admin/sys/department/' + (this.editForm.id === '' ? 'save' : 'update'), this.editForm).then(res => {
        this.$message({
          showClose: true,
          message: '创建成功！',
          type: "success",
        })
        this.HandlerClose()
        this.getDepartments()
      })
    },
    deleteHandle(id){
      this.$axios.delete('/admin/sys/department/del/'+id).then( res =>{
        this.$message({
          showClose: true,
          message: '删除成功！',
          type: "success",
        })
        this.getDepartments()
      })
    }
  },
  created() {
    this.getDepartments()
  }
}
</script>

<style scoped>

</style>
