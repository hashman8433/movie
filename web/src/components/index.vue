<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <ul>
      
      
      <li><el-button  @click="checkImgIsExist()" class="nav-link">重置视频状态</el-button></li>
      <br/>
      <br/>
      <li><el-button  @click="updateFiles()" class="nav-link">扫描新视频</el-button></li>
    	<br/>
    	<br/>
      <li><el-button  @click="generateImg()" class="nav-link">生成图片</el-button></li>
    	<br/>
    	<br/>
    	<li><el-button  @click="toSystemConfig()" class="nav-link">配置系统参数</el-button></li>
    	<br/>
    	<br/>
      <li><router-link to="/videoUpload" class="nav-link">上传视频</router-link></li>
    	<br/>
    	<br/>
      <li><router-link to="/videoList" class="nav-link">视频列表</router-link></li>
    	<br/>
    	<br/>
    </ul>
  </div>
</template>

<script>
	const axios = require('axios');
		
	export default {
	  name: '家庭影院',
	  data () {
	    return {
	      msg: '家庭影院'
	    }
	  },
	  methods: {
	  	checkImgIsExist() {
	  		var outThis = this
		  	axios.post('/api/movie/videoFile/checkImgIsExist', {})
				.then(function(response) {
					outThis.$message({
						type: 'success',
						message: '正在生成中 请稍后~'
						});
				})
				.catch(function(error) {
					console.log(error);
				});
			},
			updateFiles() {
	  		var outThis = this
		  	axios.post('/api/movie/videoFile/updateFiles', {})
				.then(function(response) {
					outThis.$message({
						type: 'success',
						message: '正在生成中 请稍后~'
						});
				})
				.catch(function(error) {
					console.log(error);
				});
			},
	  	generateImg() {
	  		var outThis = this
		  	axios.post('/api/movie/videoFile/generateImg', {})
				.then(function(response) {
					outThis.$message({
						type: 'success',
						message: '正在生成中 请稍后~'
						});
				})
				.catch(function(error) {
					console.log(error);
				});
			},
			toSystemConfig() {
				 this.$router.push('/system/config')
			}
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
