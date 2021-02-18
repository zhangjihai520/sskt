<template>
  <div class="homeWorkWrap" :style="{ minHeight: windowHeight + 'px' }">
    <tab class="titleTabWrap" :line-width="2" v-if="courseList.length > 0">
      <tab-item
        class="titleItem"
        :selected="index == 0"
        v-for="(item, index) in courseList"
        :key="item.Id"
        @on-item-click="onTabClick(item.Id)"
        >{{ item.LongName }}</tab-item
      >
    </tab>
    <group>
      <template v-for="(subject, i) in filtedSubjectList">
        <cell
          class="subject-item"
          :key="subject.SubjectId"
          :title="subject.SubjectName"
          is-link
          :border-intent="i === 0 ? false : true"
          :arrow-direction="
            curSubject.indexOf(subject.SubjectId) > -1 ? 'down' : 'up'
          "
          @click.native="subjectOpen(subject.SubjectId)"
        ></cell>
        <template v-if="curSubject.indexOf(subject.SubjectId) > -1">
          <cell
            class="examset-item"
            v-for="(examSet, j) in subject.ExamSetList"
            :key="examSet.ExamSetId"
            :title="
              filterExamSetTypeId(examSet.ExamSetTypeId) + examSet.ExamSetName
            "
            :value="filterIsFinish(examSet.IsFinish)"
            :link="filterLink(examSet)"
            :border-intent="j === 0 ? false : true"
          ></cell>
          <cell
            class="examset-item"
            title="暂无数据"
            :key="subject.SubjectId"
            v-if="!subject.ExamSetList || subject.ExamSetList.length == 0"
          ></cell>
        </template>
      </template>
    </group>
  </div>
</template>

<script>
import { Tab, TabItem, Group, Cell, CellBox } from "vux";
import { getGradeList, getStudentSubjectListH5 } from "@/services/getData";
import { cookie } from "vux";
import _ from "lodash";
import { parse } from "path";

export default {
  name: "HomeworkList",
  data() {
    return {
      /**
       * 学科列表
       */
      courseList: [],
      /**
       * 课程列表
       */
      subjectList: [],
      /**
       * 当前选中课程
       */
      curSubject: [],
      currentSubject: 0,
      filtedSubjectList: [],
      windowHeight: window.innerHeight - 32 * 1.5625
    };
  },
  components: {
    Tab,
    TabItem,
    Group,
    Cell,
    CellBox
  },
  created() {
    this.loadData();
  },
  methods: {
    getCurrentSubject() {
      if (this.currentSubject != 0) {
        this.filtedSubjectList = _.filter(this.subjectList, {
          CourseId: parseInt(this.currentSubject)
        });
        console.log(this.subjectList, this.filtedSubjectList);
      } else {
        this.filtedSubjectList = this.subjectList;
      }
    },
    filterLink(examSet) {
      return examSet.IsFinish
        ? `/${this.$store.state.BasePath}/Student/EvaluationResults?ExamSetId=${examSet.ExamSetId}`
        : `/${this.$store.state.BasePath}/Student/HomeworkDetail?ExamSetId=${examSet.ExamSetId}`;
    },
    filterIsFinish(id) {
      return id ? "测评报告" : "进入测试";
    },
    filterExamSetTypeId(id) {
      switch (id.toString()) {
        case "1":
          return "【课前】";
        case "2":
          return "【随堂】";
        case "3":
          return "【课后】";
        default:
          return "";
      }
    },
    /**
     * 加载初始数据
     */
    async loadData() {
      await getGradeList().then(res => {
        if (res != null) {
          res.CourseList.unshift({ LongName: "全部", Id: "0" });
          this.courseList = res.CourseList;
        }
      });
      this.getSubject(0);
    },
    /**
     * 获取课程列表
     * @param {Object} courseId 学科ID
     */
    async getSubject(courseId) {
      const self = this;
      await getStudentSubjectListH5({ courseId: courseId }).then(res => {
        if (res != null) {
          self.subjectList = res.SubjectList;
          this.getCurrentSubject();
        }
      });
    },
    onTabClick(courseId) {
      this.currentSubject = courseId;
      console.log(this.currentSubject);
      this.getCurrentSubject();
    },
    /**
     * 课程展开、关闭
     */
    subjectOpen(subjectId) {
      if (this.curSubject.indexOf(subjectId) > -1) {
        this.curSubject.splice(this.curSubject.indexOf(subjectId), 1);
      } else {
        this.curSubject.push(subjectId);
      }
    }
  }
};
</script>

<style lang="less" scoped>
.subject-item {
  font-size: 28px;
  &.weui-cell {
    padding-left: 10px;
  }
  &.weui-cell:before {
    left: 0;
  }
}
.examset-item {
  font-size: 28px;
  &.weui-cell:before {
    left: 15px;
  }
}

.homeWorkWrap {
  padding: 0 25px 40px;
  overflow-y: auto;
  background: url("~/static/images/openclass/bg.jpg") no-repeat;
  background-size: 100% 100%;
  .titleTabWrap {
    /deep/ .vux-tab-container {
      .vux-tab {
        background: transparent;
        padding: 0;
      }
      .vux-tab .vux-tab-item.vux-tab-selected {
        position: relative;
        border: none;
        &::after {
          content: '';
          display: block;
          width: 27px;
          height: 5px;
          background-color: #fff;
          border-radius: 3px;
          position: absolute;
          bottom: 0px;
          left: 50%;
          margin-left: -13.5px;
          transition: all 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);
        }
      }
      .vux-tab-ink-bar {
        background: none;
      }
    }

    .titleItem {
      font-size: 36px;
      color: #fff;
      padding: 0 10px;
      box-sizing: content-box;
      background: none;
    }
  }

  /deep/ .weui-cells {
    margin-top: 0.8rem;
    border-radius: 5px;
  }
}
</style>
