<template>
	<dev>
   		<h3>系统配置</h3>
   		
   		<el-form :inline="true"	>
			<el-form-item label="扫描路径" label-width="auto">
				<el-input v-model="form.filePath" label-width="60%" @change="onSubmit"> </el-input>
			</el-form-item>
			<el-form-item label="文件后缀" label-width="auto">
				<el-input v-model="form.videoFileSuffix" label-width="60%" @change="onSubmit"> </el-input>
			</el-form-item>
			</br>
			
			<el-form-item label-width="100%">
				<el-button type="primary" @click="onSubmit">保存</el-button>
			</el-form-item>
			
		</el-form>
   		
    </dev>
</template>

<script>
  	
  	const axios = require('axios');
  	
    export default {
      data() {
        return {
        	form: {
	        	filePath: '',
	        	videoFileSuffix: ''
    	    },
    	    items: []
        }
      },
      created () {
      		this.item()
		},
      methods: {
      	item() {
    		var outThis = this;
		  	axios.post('/api/movie/system/config/get', {})
			  .then(function (response) {
			  	outThis.items = response.data.data;
			  	var configList = response.data.data;
			  	for (var i = 0; configList.length; i++) {
			  		if (configList[i].code == "filePath") {
			  			outThis.form.filePath = configList[i].value;
			  		} else if (configList[i].code == "videoFileSuffix") {
			  			outThis.form.videoFileSuffix = configList[i].value;
			  		}
			  		
			  	}
			  })
			  .catch(function (error) {
			    console.log(error);
			  });
		},
		onSubmit() {
			var outThis = this;

			var systemConfigList = [
				{"code" : "filePath", "value": this.form.filePath},
				{"code" : "videoFileSuffix", "value": this.form.videoFileSuffix}
			]

			axios({
			// ('/api/movie/system/config/update', {
			// 		a
			// 		// systemConfigList
			// 	})
				url: '/api/movie/system/config/update',
				method:'post',
				data: JSON.stringify(systemConfigList),
				headers:
					{
						'Content-Type': 'application/json'
					}
			})
				
			  .then(function (response) {
			  	outThis.items = response.data.data;
			  	var configList = response.data.data;
			  	for (var i = 0; configList.length; i++) {
			  		if (configList[i].code == "filePath") {
			  			outThis.form.filePath = configList[i].value;
			  		} else if (configList[i].code == "videoFileSuffix") {
			  			outThis.form.videoFileSuffix = configList[i].value;
			  		}
			  		
			  	}
			  })
			  .catch(function (error) {
			    console.log(error);
			  });
			
			
			console.log(1232);
		}
	  }
	    
    }
</script>
<style scoped>
.el-table .cell {
  white-space: pre-line;
}
</style>