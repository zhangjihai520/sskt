<template>
  <div class="contentWrap" :style="{ minHeight: windowHeight + 'px' }">
    <div class="content">
      <div class="descWrap">
        <div class="leftDesc">
          <div class="schoolName">{{ modelData.SchoolName }}</div>
          <div class="gradeName">{{ modelData.GradeName }}</div>
          <div class="peopleName">{{ modelData.UserTrueName }}</div>
        </div>
        <img
          class="avator"
          v-bind:src="
            modelData.UserFace
              ? modelData.UserFace
              : '/static/images/common/img_user_default.png'
          "
        />
      </div>
      <timeline>
        <timeline-item
          v-for="(i, index) in modelData.StudentStudyRecordList"
          :key="index"
        >
          <p style="font-size:0.8em;">
            {{ i.Content }} {{ i.CreateDateTime }}
            <a
              class="activeBtn"
              v-if="i.StudyRecordTypeId != 4"
              v-on:click="gotoPage(i.ExamSetId)"
              >查看测评</a
            >
          </p>
        </timeline-item>
      </timeline>
    </div>
  </div>
</template>

<script>
import { Timeline, TimelineItem } from "vux";
import { getStudentStudyRecords } from "../../../services/getData";

export default {
  name: "StudentContrail",
  components: { Timeline, TimelineItem },
  data() {
    return {
      modelData: {},
      windowHeight: window.innerHeight - 50
    };
  },
  created() {
    this.getStudentStudyRecords();
  },
  methods: {
    getStudentStudyRecords() {
      let _this = this,
        param = {
          StudentId: _this.$route.params.StudentId
        };
      getStudentStudyRecords(param).then(res => {
        if (res != null) {
          _this.modelData = res;
        }
      });
    },
    gotoPage(examSetId) {
      let _this = this;
      this.$router.push(
        `/${this.$store.state.BasePath}/Teacher/QuestionResult?ExamSetId=${examSetId}&StudentId=${_this.$route.params.StudentId}`
      );
    }
  },
  filters: {
    filterStudyRecordTypeId(val) {
      switch (val.toString()) {
        case "1":
          return "课前作业";
        case "2":
          return "随堂练习";
        case "3":
          return "课后作业";
        case "4":
          return "课程";
      }
    }
  }
};
</script>

<style lang="less" scoped>
.contentWrap {
  padding: 0 40px;
  overflow-y: auto;
  background: url("~/static/images/openclass/bg.jpg") no-repeat;
  background-size: 100% 100%;
  .content {
    background: #fff;
    border-radius: 16px;
    min-height: 519px;
    padding: 40px 74px;
    margin-top: 40px;
    .descWrap {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-bottom: 60px;
      border-bottom: 1px solid #e8e8e8;
      margin-bottom: 60px;
      .leftDesc {
        .schoolName {
          font-size: 30px;
          color: #999;
        }
        .gradeName {
          font-size: 30px;
          color: #999;
        }
        .peopleName {
          font-size: 42px;
          font-weight: bold;
          color: #333;
        }
      }
      .avator {
        width: 130px;
        height: 130px;
      }
    }
    /deep/ .vux-timeline {
      padding: 0;
    }
    .activeBtn {
      font-size: 24px;
      color: #3f87fe;
      float: right;
    }
  }
}

.face {
  width: 80px;
  margin: 20px auto 0;
  display: block;
  border-radius: 50%;
  -webkit-border-radius: 50%;
  -moz-border-radius: 50%;
}
</style>
