<template>
  <div class="studentListWrap" :style="{ minHeight: windowHeight + 'px' }">
    <div class="listWrap" v-if="list.length > 0">
      <div
        class="item"
        v-for="(item, index) in list"
        :key="index"
        v-on:click="onItmeClick(item)"
      >
        <img
          class="avator"
          :src="
            item.UserFace
              ? item.UserFace
              : '/static/images/common/student_default_avator.png'
          "
        />
        <span class="name">{{ item.StudentName }}</span>
        <span class="arrow"></span>
      </div>
    </div>
    <div class="nodata" v-else>
      暂无数据
    </div>
    <div
      class="getMoreBtn"
      v-if="list.length < count && !loading"
      v-on:click="getMore"
    >
      加载更多
    </div>
    <div class="getMoreBtn" v-if="loading">
      加载中
      <inline-loading></inline-loading>
    </div>
  </div>
</template>

<script>
import { Panel, InlineLoading } from "vux";
import { getStudentList } from "../../../services/getData";

export default {
  name: "StudentList",
  components: { Panel, InlineLoading },
  data() {
    return {
      count: 0,
      pageSize: 20,
      pageIndex: 1,
      list: [],
      loading: false,
      windowHeight: window.innerHeight - 50
    };
  },
  created() {
    this.getStudentList();
  },
  methods: {
    async getStudentList() {
      let _this = this,
        param = {
          TypeEnum: "1",
          SubjectId: _this.$route.params.SubjectId,
          PageSize: _this.pageSize,
          PageIndex: _this.pageIndex
        };
      _this.loading = true;
      await getStudentList(param).then(res => {
        _this.loading = false;
        if (res != null) {
          let data = res.StudentList;
          data.map(function(value, index, array) {
            _this.list.push(value);
          });
          _this.count = res.Count;
        }
      });
    },
    onItmeClick(item) {
      this.$router.push(
        `/${this.$store.state.BasePath}/Teacher/StudentContrail/${item.StudentId}`
      );
    },
    getMore() {
      this.pageIndex++;
      this.getStudentList();
    }
  }
};
</script>

<style lang="less" scoped>
.studentListWrap {
  padding: 0 24px;
  overflow-y: auto;
  background: url("~/static/images/openclass/bg.jpg") no-repeat;
  background-size: 100% 100%;

  .listWrap {
    margin: 24px 0;
    background: #fff;
    border-radius: 16px;
    .item {
      display: flex;
      height: 90px;
      align-items: center;
      margin: 0 20px;
      border-bottom: 1px solid #efefef;
      .avator {
        width: 52px;
        height: 52px;
      }
      .name {
        flex: 1;
        font-size: 36px;
        color: #333;
        margin-left: 20px;
      }
      .arrow {
        position: relative;
        &:after {
          content: " ";
          display: inline-block;
          height: 12px;
          width: 12px;
          border-width: 4px 4px 0 0;
          border-color: #c8c8cd;
          border-style: solid;
          transform: matrix(0.71, 0.71, -0.71, 0.71, 0, 0);
          position: relative;
          top: -4px;
          position: absolute;
          top: 50%;
          margin-top: -8px;
          right: 4px;
        }
      }
    }
  }

  .getMoreBtn {
    font-size: 30px;
    color: #fff;
    margin-top: 30px;
    padding-bottom: 30px;
    text-align: center;
  }

  .nodata {
    text-align: center;
    line-height: 400px;
    color: #fff;
  }
}
</style>
