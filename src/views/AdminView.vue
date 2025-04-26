<template>
  <div>
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.username" style="width: 200px; margin-right: 10px" placeholder="请输入姓名"></el-input>
      <el-input v-model="params.email" style="width: 200px; margin-right: 10px" placeholder="请输入邮箱"></el-input>
      <el-button type="warning" @click="findBySearch()">搜索</el-button>
      <el-button type="warning" @click="reset()">重置</el-button>
      <el-button type="primary" @click="add()">新增</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px">
        <el-table-column prop="username" label="姓名"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column prop="role" label="权限"></el-table-column>
        <el-table-column label="创建时间">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>

        <el-table-column label="更新时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.updatedAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="phoneNumber" label="电话"></el-table-column>
        <el-table-column prop="realName" label="真实姓名"></el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="deletce(scope.row.id)">
              <template slot="reference">
                <el-button type="danger" style="margin-left: 5px;">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页 -->
    <div class="block">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="params.pageNum" :page-sizes="[5, 10, 15, 20]" :page-size="params.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <el-dialog title="请填写账户信息" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form :model="form">
        <el-form-item label="名称" :label-width="formLabelWidth">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-table-column prop="role" label="角色"></el-table-column>
        <el-form-item label="权限" label-width="15%">
          <el-select v-model="form.role" placeholder="请选择" style="width: 90%">
            <el-option label="企业" value="ROLE_COMPANY"></el-option>
            <el-option label="学生" value="ROLE_STUDENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Email" :label-width="formLabelWidth">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" :label-width="formLabelWidth">
          <el-input v-model="form.phoneNumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" :label-width="formLabelWidth">
          <el-input v-model="form.realName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button @click="submit()" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import dayjs from 'dayjs';

export default {
  name: "AdminView",
  data() {
    return {
      params: {
        username: '',
        email: '',
        pageNum: 1,
        pageSize: 5

      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {},
      formLabelWidth: '80px',


    }

  },

  //页面创建完成时执行
  created() {
    this.findBySearch();
    
  },
  //页面挂载完成时执行
  methods: {
    // 日期格式化方法
    formatDate(date) {
      return dayjs(date).format('YYYY-MM-DD');
    },
    formatDateTime(datetime) {
      return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss');
    },
    findBySearch() {
      request.get('/admin/search', {
        params: this.params
      }).then(res => {
        if (res.code == '200') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        }
        else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      })
    },
    add() {
      this.form = {};
      this.dialogFormVisible = true;
    },
    edit(row) {
      this.form = row;
      this.dialogFormVisible = true;
    },

    reset() {
      this.params = {
        username: '',
        email: '',
        pageNum: '1',
        pageSize: '5',
      }
      this.findBySearch();
    },
    handleSizeChange(pageSize) {
      this.params.pageSize = pageSize;
      this.findBySearch();
    },
    handleCurrentChange(pageNum) {
      this.params.pageNum = pageNum;
      this.findBySearch();
    },
    submit() {
      request.post('/admin', this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            message: ('操作成功'),
            type: 'success'
          });
          this.dialogFormVisible = false;
          this.findBySearch();
        }
        else {
          this.$message({
            message: (res.msg),
            type: 'error'
          });
        }
      })
    }
    ,
    deletce(id) {
      request.delete('/admin/' + id).then(res => {
        if (res.code == '200') {
          this.$message({
            message: ('删除成功'),
            type: 'success'
          });
          this.findBySearch();
        }
        else {
          this.$message({
            message: (res.msg),
            type: 'error'
          });
        }
      })
    }
  }
}
</script>