<template>
  <div class="page" :style="{ minHeight: windowHeight + 'px' }">
    <div class="exam-name">{{ examSetName }}</div>
    <div class="chart-title">知识点占比</div>
    <div class="chart">
      <canvas height="260" class="noselect" ref="chart"></canvas>
      <div class="my-score">
        他的成绩:
        <span>{{ evalautionResult && evalautionResult.CorrectCount }}</span
        >/{{ evalautionResult && evalautionResult.QuestionCount }}
      </div>
    </div>
    <div class="title"><i class="iconfont timufenxi"></i> 知识点掌握分析</div>
    <div class="x-table-wrap">
      <x-table
        class="xtable"
        full-bordered
        v-if="
          evalautionResult &&
            evalautionResult.KnowledgePointMasterAnalysis.length > 0
        "
      >
        <thead>
          <tr>
            <th width="14%">掌握程度</th>
            <th width="29%">二级知识点</th>
            <th width="29%">三级知识点</th>
            <th width="14%">难度系数</th>
            <th width="14%">题目数量</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="item in evalautionResult.KnowledgePointMasterAnalysis"
            :key="item.KnowledgePointName"
          >
            <td v-if="item.MasterAnalysis == 1">已掌握</td>
            <td v-if="item.MasterAnalysis == 2">部分掌握</td>
            <td v-if="item.MasterAnalysis == 3">未掌握</td>
            <td>{{ item.ParentKnowledgePointName }}</td>
            <td>{{ item.KnowledgePointName }}</td>
            <td v-if="item.KnowledgePointLevel == 1">容易</td>
            <td v-if="item.KnowledgePointLevel == 2">较易</td>
            <td v-if="item.KnowledgePointLevel == 3">一般</td>
            <td v-if="item.KnowledgePointLevel == 4">较难</td>
            <td v-if="item.KnowledgePointLevel == 5">困难</td>
            <td>{{ item.NumberOfQuestion }}</td>
          </tr>
        </tbody>
      </x-table>
    </div>
    <div class="title"><i class="iconfont timufenxi"></i> 答题记录</div>
    <div class="question-records" v-if="questions && questions.length > 0">
      <div
        class="question-item"
        v-for="(item, index) in questions"
        :key="item.QuestionId"
      >
        <div class="question-title">
          <span class="q_num">{{ index + 1 }}.</span>
          <div v-html="item.QuestionContent"></div>
        </div>
        <div class="options">
          <ul>
            <li v-for="q in item.Options" :key="q.OrderIndex">
              <span class="label">{{ q.QuestionOptionId }}</span>
              <div v-html="q.QuestionOptionText"></div>
            </li>
          </ul>
        </div>
        <div class="answer">
          学生作答：
          <span :class="[item.IsCorrect ? 'correct' : 'error']">{{
            item.StudentAnswer ? item.StudentAnswer : "-"
          }}</span>
          正确答案：
          <span>{{ item.Answer }}</span>
          <span
            class="show-analysis"
            @click="toggleAnalysisShow(item)"
            v-if="item.isOpenAnalysis"
          >
            收起解析
            <i class="iconfont shouqi"></i>
          </span>
          <span class="show-analysis" @click="toggleAnalysisShow(item)" v-else>
            展开解析
            <i class="iconfont zhankai"></i>
          </span>
        </div>
        <div
          class="analysis"
          v-show="item.isOpenAnalysis"
          v-html="item.Analysis"
        ></div>
      </div>
    </div>
    <div v-transfer-dom>
      <loading :show="loading" text="加载中..."></loading>
    </div>
    <div v-transfer-dom>
      <alert v-model="show" title="" @on-hide="onHide"> {{ msg }}</alert>
    </div>
  </div>
</template>

<script>
import {
  XTable,
  Alert,
  Loading,
  TransferDomDirective as TransferDom
} from "vux";
import {
  getEvaluationResult,
  getQuestionDetails
} from "../../../services/getData";
import F2 from "@antv/f2";
const PieLabel = require("@antv/f2/lib/plugin/pie-label"); // 引入 PieLabel 模块

export default {
  directives: {
    TransferDom
  },
  components: {
    XTable,
    Alert,
    Loading
  },
  data() {
    return {
      query: {
        ExamSetId: "", // 作业ID
        StudentId: "" // 学生ID
      },
      examSetName: "", // 试卷名称
      evalautionResult: null,
      questions: [],
      names: "",
      total: 0,
      loading: true,
      msg: "",
      show: false,
      windowHeight: window.innerHeight - 32 * 1.5625
    };
  },
  created() {
    this.query.ExamSetId = this.$route.query.ExamSetId;
    this.query.StudentId = this.$route.query.StudentId;
  },
  mounted() {
    const windowWidth = window.innerWidth - 32 * 0.625;
    const windowHeight = window.innerHeight;
    const chart = new F2.Chart({
      el: this.$refs.chart,
      width: windowWidth,
      height:
        windowWidth > windowHeight ? windowHeight - 54 : windowWidth * 0.707,
      pixelRatio: window.devicePixelRatio
    });
    this.getEvaluationResultH5(chart);
  },
  methods: {
    // 获取测评结果信息
    async getEvaluationResultH5(chart) {
      const params = {
        ExamSetId: this.query.ExamSetId,
        StudentId: this.query.StudentId
      };

      let evalautionResult = await getEvaluationResult(params);

      if (evalautionResult == null) {
        // 请求错误
        this.msg = "请求异常";
        this.show = true;
        this.loading = false;
        return false;
      }

      this.evalautionResult = evalautionResult;
      this.examSetName = this.evalautionResult.ExamSetName;

      // 获取题目信息
      this.getQuestionDetailsH5(this.query.ExamSetId);

      const chartData = this.evalautionResult.KnowledgePointScores.map(item => {
        this.total += parseFloat(item.Score);
        return {
          name: item.KnowledgePointName,
          percent: parseFloat(item.Score),
          a: "1"
        };
      });

      this.renderChart(chart, chartData);
    },
    // 获取作业的题目列表
    async getQuestionDetailsH5(examSetId) {
      const _this = this;
      const params = {
        ExamSetId: examSetId
      };
      let examResult = await getQuestionDetails(params);

      this.loading = false;

      if (examResult == null) {
        // 请求错误
        this.msg = "请求异常";
        this.show = true;
        return false;
      }
      // 合并两个请求中的题目相关的内容
      _this.questions = examResult.Questions.map(item => {
        let current = _this.evalautionResult.QuestionResult.filter(res => {
          return res.QuestionId == item.QuestionId;
        })[0];
        return {
          ...item,
          StudentAnswer: current.StudentAnswer,
          IsCorrect: current.IsCorrect,
          isOpenAnalysis: false
        };
      });
    },
    toggleAnalysisShow(data) {
      this.questions = this.questions.map(item => {
        if (item.QuestionId === data.QuestionId) {
          return { ...item, isOpenAnalysis: !data.isOpenAnalysis };
        } else {
          return item;
        }
      });
    },
    renderChart(chart, data) {
      const _this = this;
      const initGuideText =
        this.checkEllipsis(data[0].name) +
        "\n" +
        ((data[0].percent / _this.total) * 100).toFixed(1) +
        "%";
      const guide = chart.guide().text({
        position: ["50%", "50%"],
        style: {
          fill: _this.total > 0 ? "#666" : "#f00", // 点的填充颜色
          fontSize: 12
        },
        limitInPlot: true,
        content: _this.total > 0 ? initGuideText : "成绩为0，无知识点占比分析"
      });
      chart.source(data);
      chart.coord("polar", {
        transposed: true,
        innerRadius: 0.6,
        radius: 0.85
      });
      chart.legend(false);
      chart.tooltip(false);
      chart.axis(false);
      if (_this.total > 0) {
        chart.registerPlugins(PieLabel);
        chart.pieLabel({
          label1(data, color) {
            return {
              text: data.name // 文本内容
            };
          },
          activeShape: true,
          activeStyle: {
            offset: 1,
            appendRadius: 8,
            fillOpacity: 0.5
          },
          onClick(ev) {
            var data = ev.data;
            if (data) {
              guide.content =
                _this.checkEllipsis(data.name) +
                "\n" +
                ((data.percent / _this.total) * 100).toFixed(1) +
                "%";
              guide.repaint();
            }
          }
        });
        chart
          .interval()
          .position("a*percent")
          .color("name")
          .adjust("stack");
      } else {
        chart
          .interval()
          .position("a*percent")
          .color("name")
          .adjust("stack");
        chart.changeSize(null, 60);
      }
      chart.render();
    },
    checkEllipsis(str) {
      if (str.length > 8) {
        return str.substr(0, 5) + "...";
      } else {
        return str;
      }
    },
    onHide() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.page {
  background: #fff;
  font-size: 0.8em;
  padding: 0 20px;
  overflow-y: auto;
  background: url("~/static/images/openclass/bg.jpg") no-repeat;
  background-size: 100% 100%;
}
.chart {
  width: 100%;
  background: #fff;
  border-radius: 10px;
  padding-top: 20px;
}
.chart-title {
  font-size: 30px;
  color: #fff;
  margin-bottom: 20px;
  padding-left: 10px;
}
.exam-name {
  text-align: center;
  font-size: 30px;
  color: #fff;
  padding: 20px 10px 10px;
}
.my-score {
  text-align: center;
  clear: both;
  font-size: 1em;
  padding-top: 15px;
  padding-bottom: 15px;
}
.my-score span {
  color: green;
}
.title {
  font-size: 30px;
  color: #fff;
  padding: 20px 10px 10px;
}
.title .iconfont {
  color: green;
}
.x-table-wrap {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
}
.xtable {
  line-height: 1.5;
}
.xtable th {
  padding: 10px 0;
}
.xtable td {
  padding: 10px 5px;
}
.question-records {
  padding: 10px;
  background: #fff;
  border-radius: 10px;
}
.question-records .question-item {
  margin-top: 20px;
  border: solid 1px #eee;
  padding: 10px;
}
.question-records .question-item:first-child {
  margin-top: 0;
}
.question-records .question-title {
  margin-bottom: 10px;
}
.question-records .question-title .q_num {
  margin-right: 10px;
}
.question-records .question-title > div {
  display: inline;
}
.question-records .options ul {
  padding: 0;
  margin: 0;
  margin-bottom: 10px;
}
.question-records .options li {
  list-style: none;
  padding-bottom: 15px;
  display: flex;
  overflow: hidden;
  align-items: center;
}
.question-records .options li > div {
  flex: 1;
}
.question-records .options .label {
  display: inline-block;
  border: solid 1px #ccc;
  border-radius: 50%;
  text-align: center;
  margin-right: 10px;
  line-height: 36px;
  width: 36px;
  height: 36px;
}
.question-records .answer {
  overflow: hidden;
  margin-top: 5px;
  border-top: dashed 1px #ccc;
  padding-top: 5px;
}
.question-records .answer span {
  margin-right: 20px;
}
.question-records .answer span.error {
  color: #ff0000;
}
.question-records .answer span.correct {
  color: green;
}
.question-records .answer span.show-analysis {
  float: right;
  color: green;
}
.question-records .analysis {
  overflow: hidden;
}
.question-records .question-title >>> img,
.question-records .options li >>> img,
.question-records .analysis >>> img {
  max-width: 100%;
}
</style>
