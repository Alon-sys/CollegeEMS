<template>
  <div>
    <div style="margin-bottom: 15px;">
      <el-input v-model="params.title" style="width: 200px; margin-right: 10px" placeholder="请输入公告标题"></el-input>
      <el-button type="warning" @click="findBySearch">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button v-if="this.user.role === 'ROLE_ADMIN'" type="primary" @click="add">新增</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px" v-loading="loading">
        <el-table-column prop="title" label="公告标题"></el-table-column>
        <el-table-column label="公告内容">
          <template slot-scope="scope">
            <el-button type="success" @click="viewEditor(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发布时间">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.time) }}
          </template>
        </el-table-column>
        <el-table-column v-if="this.user.role === 'ROLE_ADMIN'" label="操作" width="180">
          <template slot-scope="scope">
            <el-button type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="() => deleteNotice(scope.row.id)">
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

    <el-dialog :title="form.id ? '编辑公告' : '新增公告'" :visible.sync="dialogFormVisible" width="80%">
      <el-form :model="form" label-width="100px">
        <el-form-item label="公告标题">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="公告内容">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="公告内容" :visible.sync="editorVisible" width="50%">
      <div v-html="this.viewData" class="w-e-text"></div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import dayjs from 'dayjs';
import E from 'wangeditor'

let editor
function initWangEditor(content) {
  setTimeout(() => {
    if (!editor) {
      editor = new E('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadImgServer = 'http://localhost:8888/api/files/wang/upload'
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}

export default {
  name: "NoticeView",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')) || {},
      loading: false,
      params: {
        title: '',
        pageNum: 1,
        pageSize: 5
      },
      tableData: [],
      total: 0,
      dialogFormVisible: false,
      editorVisible: false,
      form: {},
      viewData: '',
      formLabelWidth: '100px'
    }
  },
  created() {
    this.findBySearch();
  },
  methods: {
    formatDateTime(date) {
      return date ? dayjs(date).format('YYYY-MM-DD HH:mm:ss') : '-';
    },

    findBySearch() {
      request.get('/notice/search', {
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
      initWangEditor(""),
        this.dialogFormVisible = true;
    },
    edit(row) {
      this.form = { ...row };
      initWangEditor(row.content)
      this.dialogFormVisible = true;
    },
    reset() {
      this.params = {
        title: '',
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
      this.form.content = editor.txt.html()
      request.post('/notice', this.form).then(res => {
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
    },
    viewEditor(content) {
      this.viewData = content;
      this.editorVisible = true;
    },
    async deleteNotice(id) {
      try {
        const res = await request.delete(`/notice/${id}`);
        if (res.code === '200') {
          this.$message.success('删除成功');
          this.findBySearch();
        } else {
          this.$message.error(res.msg || '删除失败');
        }
      } catch (error) {
        console.error('删除失败:', error);
        this.$message.error('删除失败: ' + (error.response?.data?.message || error.message));
      }
    }
  }
}
</script>