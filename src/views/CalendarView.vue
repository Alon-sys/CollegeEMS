<template>
    <div style="margin-top: 20px; padding: 20px;">
      <div>
        <el-page-header class="page-goBack" @back="goBack" content="个人中心"></el-page-header>
      </div>
        <el-calendar v-model="value">
        </el-calendar>
    </div>
</template>

<script>
  export default {
    data() {
      return {
        value: new Date()
      }
    },
    mounted() {

    },
    methods: {
      goBack() {
        this.$router.go(-1);
      }
    }
  }
</script>