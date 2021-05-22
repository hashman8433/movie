<template>
  <dev>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="id" label="预览图片" width="300">
        <!--<img style="width:80px;" src="../../build/logo.png" />-->
        <template slot-scope="scope">
          <img
            @click="handleClick(scope.row)"
            style="width: 100%"
            :src="scope.row.filePathWeb"
          />
        </template>
      </el-table-column>
    </el-table>
  </dev>
</template>

<script>
const axios = require("axios");

export default {
  data() {
    return {
      tableData: [
        {
          imgPathWeb: "../../build/logo.png",
        },
        {
          filePath: "../../build/logo.png",
        },
        {
          filePath: "../../build/logo.png",
        },
      ],
    };
  },
  created() {
    console.log("beforeCreate");
    this.item();
  },
  methods: {
    item() {
      var outThis = this;
      console.log(this._data.num++);
      console.log(this._data);
      axios
        .post("/api/movie/imgFile/list", {
          videoFileId: outThis.$route.query.videoFileId,
        })
        .then(function (response) {
          console.log(response.data.data);
          outThis._data.tableData = response.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
    },
  },
};
</script>
<style scoped>
.el-table .cell {
  white-space: pre-line;
}
</style>