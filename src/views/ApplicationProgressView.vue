<template>
  <div>
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.username" style="width: 200px; margin-right: 10px" placeholder="请输入姓名"></el-input>
      <el-button type="warning" @click="findBySearch">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button v-if="user.role !== 'ROLE_STUDENT'" type="primary" @click="add">新增</el-button>
    </div>
    <div>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="studentName" label="姓名" width="180"></el-table-column>
        <el-table-column prop="jobTitle" label="岗位名称"></el-table-column>
        <el-table-column prop="status" label="投递状态">
          <template v-slot="{ row }">
            <el-tag v-if="row.status === '已查看'" type="warning">已查看</el-tag>
            <el-tag v-else-if="row.status === '已通过'" type="success">未就业</el-tag>
            <el-tag v-else-if="row.status === '已拒绝'" type="danger">已拒绝</el-tag>
            <el-tag v-else>已投递</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appliedAt" label="投递时间" :formatter="formatDate">
        </el-table-column>
        <el-table-column prop="updatedAt" label="投递更新时间" :formatter="formatDateTime"></el-table-column>

        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button v-if="user.role !== 'ROLE_STUDENT'" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button v-if="user.role !== 'ROLE_STUDENT'" type="primary" @click="details(scope.row.resumeId)">查看</el-button>
            <el-popconfirm v-if="user.role === 'ROLE_STUDENT'" title="确定删除吗？" @confirm="() => deleteJob(scope.row.id)">
              <template slot="reference">
                <el-button type="danger" style="margin-left: 5px;">撤销</el-button>
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

    <el-dialog :title="form.id ? '编辑投递状态' : '新增投递'" :visible.sync="dialogFormVisible" width="50%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="投递状态">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已查看" value="已查看"></el-option>
            <el-option label="已拒绝" value="已拒绝"></el-option>
            <el-option label="已通过" value="已通过"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import dayjs from 'dayjs';

export default {
  name: "ApplicationView",
  data() {
    return {
      loading: false,
      user: JSON.parse(localStorage.getItem('user')) || {},
      params: {
        username: '',
        pageNum: 1,
        pageSize: 5
      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {}
    }
  },
  created() {
    this.findBySearch();
  },
  methods: {
    formatDate(row, column, cellValue) {
      return cellValue ? dayjs(cellValue).format('YYYY-MM-DD') : '-';
    },
    formatDateTime(row, column, cellValue) {
      return cellValue ? dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss') : '-';
    },
    async findBySearch() {
      this.loading = true;
      try {
        let res;
        if (this.user.role === 'ROLE_ADMIN') {
          // 学生和管理员使用统一的搜索接口
          res = await request.get('/applications/search', { params: this.params });
        } else if (this.user.role === 'ROLE_STUDENT') {
          res = await request.get('/applications/user/' + this.user.id, {
            params: this.params
          });
        }else if (this.user.role === 'ROLE_COMPANY') {
          res = await request.get('/applications/company/' + this.user.id, {
            params: this.params
          });
        }

        if (res.code === '200') {
          // 统一处理数据结构
          this.tableData = res.data.list || res.data || [];
          this.total = res.data.total || res.data.length || 0;

          // 调试输出
          console.log('接口返回数据:', res.data);
          console.log('表格数据:', this.tableData);
        }
      } catch (error) {
        console.error('获取数据失败:', error);
        this.$message.error('获取数据失败');
      } finally {
        this.loading = false;
      }
    },
    add() {
      this.form = { status: '已投递' };
      this.dialogFormVisible = true;
    },
    edit(row) {
      this.form = { ...row };
      this.dialogFormVisible = true;
    },
    reset() {
      this.params = {
        username: '',
        pageNum: 1,
        pageSize: 5,
      };
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
      if (!this.form.status) {
        this.$message.warning('请选择投递状态');
        return;
      }

      const apiUrl = this.form.id ? '/applications' : '/applications';
      request.post(apiUrl, this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功');
          this.dialogFormVisible = false;
          this.findBySearch();
        } else {
          this.$message.error(res.msg || '操作失败');
        }
      }).catch(err => {
        this.$message.error('请求失败');
      });
    },
    deleteJob(id) {
      request.delete('/applications/' + id).then(res => {
        if (res.code === '200') {
          this.$message.success('删除成功');
          this.findBySearch();
        } else {
          this.$message.error(res.msg || '删除失败');
        }
      }).catch(err => {
        this.$message.error('请求失败');
      });
    },
    details(resumeId) {
      this.$router.push({
        path: '/resumeDetail',
        query: { resumeId },
      });
    },
  }
}
</script>