<template>
  <div>
    <!-- 搜索栏 -->
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.title" style="width: 200px; margin-right: 10px" placeholder="请输入职位标题"></el-input>
      <el-input v-model="params.location" style="width: 200px; margin-right: 10px" placeholder="请输入职位地点"></el-input>
      <el-input v-model="params.category" style="width: 200px; margin-right: 10px" placeholder="请输入职位类别"></el-input>
      <el-select v-model="form.type" placeholder="请选择">
        <el-option label="全职" value="全职"></el-option>
        <el-option label="兼职" value="兼职"></el-option>
        <el-option label="实习" value="实习"></el-option>
      </el-select>
      <el-button type="warning" @click="findBySearch()">搜索</el-button>
      <el-button type="warning" @click="reset()">重置</el-button>
      <el-button type="primary" @click="add()">新增</el-button>
    </div>

    <!-- 表格 -->
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px">
        <el-table-column prop="title" label="职位标题"></el-table-column>
        <el-table-column prop="salary" label="职位薪资" width="180"></el-table-column>
        <el-table-column prop="description" label="职位描述"></el-table-column>
        <el-table-column prop="createdAt" label="发布时间"></el-table-column>
        <el-table-column prop="updatedAt" label="更新时间"></el-table-column>
        <el-table-column prop="location" label="职位地点"></el-table-column>
        <el-table-column prop="category" label="职位类别"></el-table-column>
        <el-table-column prop="type" label="职位类型"></el-table-column>
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

    <!-- 弹窗 -->
    <el-dialog title="请填写账户信息" :visible.sync="dialogFormVisible" width="30%" center>
      <el-form :model="form">
        <el-form-item label="职位标题" :label-width="formLabelWidth">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="职位薪资" :label-width="formLabelWidth">
          <el-input v-model="form.salary" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="职位描述" :label-width="formLabelWidth">
          <el-input v-model="form.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4 }"></el-input>
        </el-form-item>
        <el-form-item label="职位地点" :label-width="formLabelWidth">
          <el-input v-model="form.location" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="职位类别" :label-width="formLabelWidth">
          <el-input v-model="form.category" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="职位类型" :label-width="formLabelWidth">
          <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
            <el-option label="全职" value="全职"></el-option>
            <el-option label="兼职" value="兼职"></el-option>
            <el-option label="实习" value="实习"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button @click="submit()" type="primary">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: "JobView",
  data() {
    return {
      params: {
        title: '',
        location: '',
        category: '',
        type: '',
        pageNum: 1,
        pageSize: 5
      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      form: {}
    };
  },
  created() {
    this.findBySearch();
  },
  methods: {
    findBySearch() {
      request.get('/jobs/search', { params: this.params }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      });
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
        title: '',
        location: '',
        category: '',
        type: '',
        pageNum: 1,
        pageSize: 5
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
      request.post('/jobs', this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            message: '操作成功',
            type: 'success'
          });
          this.dialogFormVisible = false;
          this.findBySearch();
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      });
    },
    deletce(id) {
      request.delete(`/jobs/${id}`).then(res => {
        if (res.code === '200') {
          this.$message({
            message: '删除成功',
            type: 'success'
          });
          this.findBySearch();
        } else {
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      });
    }
  }
};
</script>