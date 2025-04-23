<template>
    <div>
  <div style="margin-bottom: 15px;">
      <el-input v-model="params.username" style="width: 200px; margin-right: 10px" placeholder="请输入公司名称"></el-input>
      <el-input v-model="params.email" style="width: 200px; margin-right: 10px" placeholder="请输入所属行业"></el-input>
      <el-button type="warning" @click="findBySearch()">搜索</el-button>
      <el-button type="warning" @click="reset()">重置</el-button>
      <el-button type="primary" @click="add()">新增</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%; margin: 15px 0px">
    
        <el-table-column label="图书封面">
         <template v-slot="scope">
         <el-image
        style="width: 70px; height: 70px; border-radius: 50%"
        :src="'http://localhost:8888/api/files/' + scope.row.logo"
        :preview-src-list="['http://localhost:8888/api/files/' + scope.row.logo]">
          </el-image>
        </template>
        </el-table-column>
        <el-table-column prop="name" label="公司名称"></el-table-column>
        <el-table-column prop="website" label="公司官网" width="180" ></el-table-column>
        <el-table-column prop="description" label="公司简介" width="200"  ></el-table-column>
        <el-table-column prop="industry" label="所属行业"></el-table-column>
        <el-table-column prop="size" label="公司规模"></el-table-column>
       
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
  
    <el-dialog title="请填写公司信息" :visible.sync="dialogFormVisible" width="30%" center >
    <el-form :model="form">
      <el-form-item label-position="top" label="公司logo" label-width="20%" >
     <el-upload action="http://localhost:8888/api/files/upload" :on-success="successUpload">
     <el-button size="small" type="primary">点击上传</el-button>
       </el-upload>
      </el-form-item>
      <el-form-item label="名称" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="用户id" :label-width="formLabelWidth">
        <el-input v-model="form.userID" autocomplete="off"></el-input>
      </el-form-item>
    
      <el-form-item label="公司官网" :label-width="formLabelWidth">
        <el-input v-model="form.website" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="公司简介" :label-width="formLabelWidth">
        <el-input  type="textarea" v-model="form.description" autocomplete="off"></el-input>
      </el-form-item>
        <el-form-item label="所属行业" :label-width="formLabelWidth">
            <el-input v-model="form.industry" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="公司规模" :label-width="formLabelWidth">
            <el-input v-model="form.size" autocomplete="off"></el-input>
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
         request.get('/company/search',{
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
        add(){
          this.form={};
          this.dialogFormVisible = true;
        },
        edit(row){
          this.form = row;
          this.dialogFormVisible = true;
        },
        
        reset(){
        this.params={
          username:'',
          email:'',
          pageNum:'1',
          pageSize:'5',
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
        submit(){
        request.post('/company',this.form).then(res=>{
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
      }
      ,
        deletce(id){
          request.delete('/company/'+id).then(res=>{
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