<template>
    <div>
  <div style="margin-bottom: 15px;">
      <el-input v-model="params.username" style="width: 200px; margin-right: 10px" placeholder="请输入公司名称"></el-input>
      <el-input v-model="params.email" style="width: 200px; margin-right: 10px" placeholder="请输入所属行业"></el-input>
        <el-input v-model="params.role" style="width: 200px; margin-right: 10px" placeholder="请输入公司规模"></el-input>
      <el-button type="warning" @click="findBySearch()">搜索</el-button>
      <el-button type="warning" @click="reset()">重置</el-button>
      <el-button type="primary" @click="add()">新增</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px">
        <el-table-column prop="studentID" label="学号" ></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="age" label="年龄"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="college" label="学院"></el-table-column>
        <el-table-column prop="major" label="专业"></el-table-column>
        <el-table-column prop="classInfo" label="班级"  width="200"></el-table-column>
        <el-table-column prop="isEmployed" label="是否就业">
            <template v-slot="scope">
                <el-tag v-if="scope.row.isEmployed == 1" type="success">已就业</el-tag>
                <el-tag v-else type="info">未就业</el-tag>
            </template>
        </el-table-column>
       
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
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="params.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  
    <el-dialog title="请填学生信息" :visible.sync="dialogFormVisible" width="30%" center >
    <el-form :model="form" :label-position="top">
        <el-form-item label="学号" :label-width="formLabelWidth">
        <el-input v-model="form.studentID" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="学生姓名" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="年龄" :label-width="formLabelWidth">
        <el-input v-model="form.age" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <template>
        <el-radio v-model="form.gender" label="男">男</el-radio>
        <el-radio v-model="form.gender" label="女">女</el-radio>
    </template>
        </el-form-item>
        <el-form-item label="学院" :label-width="formLabelWidth">
            <el-input v-model="form.college" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业" :label-width="formLabelWidth">
            <el-input v-model="form.major" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级" :label-width="formLabelWidth">
            <el-input v-model="form.studentClass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="就业情况">
            <el-radio-group v-model="form.isEmployed">
      <el-radio-button label="1">已就业</el-radio-button>
      <el-radio-button label="0" >未就业</el-radio-button>
      </el-radio-group>
        </el-form-item>

        

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button  @click="dialogFormVisible = false">取 消</el-button>
      <el-button  @click="submit()" type="primary">确 定</el-button>
    </div>
  </el-dialog>
    </div>
  </template>
  
    <script>
  import request from '@/utils/request';
  
    export default {
      name: "CompanyView",
      data() {
        return {
          params:{
            name:'',
            industry:'',
            size:'',
            pageNum:1,
            pageSize:5
          
          },
          tableData:[],
          total:0,
          dialogFormVisible:false,
          form:{},
         
        }
      
      },
  
      //页面创建完成时执行
      created(){
        this. findBySearch();
      },
      //页面挂载完成时执行
      methods:{
        findBySearch(){
         request.get('/student/search',{
          params:this.params
        }).then(res=>{
          if(res.code== '200'){
            this.tableData = res.data.list;
            this.total = res.data.total;
          }
          else{
            this.$message({
          message: res.msg,
          type: 'error'
        });
          }
  
         })
          
        },
        //新增操作
        //清空表单数据
        add(){
          this.form={};
          this.dialogFormVisible = true;
        },
        //编辑操作
        //将选中行的数据赋值给表单数据
        edit(row){
          this.form = row;
          this.dialogFormVisible = true;
        },
        
        //重置操作
        reset(){
        this.params={
          username:'',
          email:'',
          pageNum:'1',
          pageSize:'5',
        }
        this.findBySearch();
      },
  
      //分页操作
      // pageNum:当前页码，pageSize:每页显示条数
        handleSizeChange(pageSize) {
          this.params.pageSize = pageSize;
          this.findBySearch();
      },
        handleCurrentChange(pageNum) {
         this.params.pageNum = pageNum;
         this.findBySearch();
      },
        //新增和编辑操作
        submit(){
        request.post('/student',this.form).then(res=>{
        if(res.code === '200'){
        this.$message({
            message: ('操作成功'),
            type: 'success'
        });
        this.dialogFormVisible = false;
        this.findBySearch();
      }
      else{
        this.$message({
          message: (res.msg),
          type: 'error'
        });
      }
      })
      },
        //删除操作
        deletce(id){
          request.delete('/student/'+id).then(res=>{
            if(res.code == '200'){
              this.$message({
                message: ('删除成功'),
                type: 'success'
              });
              this.findBySearch();
            }
            else{
              this.$message({
                message: (res.msg),
                type: 'error'
              });
            }
          })
        },
        successUpload(res) {
        this.form.logo = res.data;
        },
     }
    }
  </script> 