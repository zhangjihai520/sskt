<template>
  <div ref="swiperCase">
    <!-- 主体内容 -->
    <Flexbox class="topDetail" align="center">
      <flexbox-item>
        <span class="title">单项选择</span>
      </flexbox-item>
      <flexbox-item class="fright">
        <span class="currentNum">{{currentNum}}</span>
        / {{practiceList.length}}
        <i class="iconfont Analysis" @click="drawerChange"></i>
      </flexbox-item>
    </Flexbox>
    <div class="swiperItem" v-for="(item, index) in practiceList" :key="item.QuestionId">
      <div v-if="currentNum==item.Order">
        <div class="questionTitle" v-html="`${item.Order}、${item.QuestionContent}`"></div>
        <div class="divider"></div>
        <div class="optionSet">
          <div
            v-for="(option, j) in item.Options"
            :key="j"
            @click="selectAnswer(item.QuestionId, option.QuestionOptionId, item.Order)"
          >
            <span
              :class="(item.QuestionId == answerList[index].QuestionId && option.QuestionOptionId == answerList[index].Answer) ? 'letter active' : 'letter'"
            >{{option.QuestionOptionId}}</span>
            <div style="display:inline;" v-html="option.QuestionOptionText"></div>
          </div>
          <x-button
            v-if="index === practiceList.length - 1"
            class="submitCls"
            type="primary"
            @click.native="submitAnswer"
          >提交答案</x-button>
        </div>
      </div>
    </div>
    <div style="height:46px;"></div>
    <!-- 下拉框 -->
    <Popup
      class="PopupSlot"
      v-model="drawerVisibility"
      width="100%"
      position="top"
      @on-hide="drawerVisibility = false"
    >
      <!-- 菜单内容 -->
      <span
        v-for="(item, index) in answerList"
        :class="item.QuestionId ? 'optionter active' : 'optionter'"
        @click="pageChange(index+1)"
        :key="index"
      >{{ index+1 }}</span>
    </Popup>


    <confirm 
      v-model="confirmStatus"
      title="提示"
      @on-cancel="onCancel"
      @on-confirm="onConfirm"
      confirm-text="坚持提交"
      cancel-text="继续作答"
    >
      <p style="text-align:center;">还有未作答的题目</p>
    </confirm>
  </div>
</template>

<script>
import { Icon, Flexbox, FlexboxItem, XButton, Toast, Popup, Confirm } from "vux";
import { getQuestionDetails, submitExamSet } from "../../../services/getData";
export default {
  name: "QuestionPractice",
  data() {
    return {
      /**题目列表 */
      questionList: [],
      /**当前题号 */
      currentNum: 1,
      /**提交答案数据 */
      answerList: [],
      /**抽屉的显示隐藏状态 */
      drawerVisibility: false,
      /**弹框的显示隐藏状态 */
      confirmStatus: false
    };
  },
  created() {
    const ExamSetId = this.$route.query.ExamSetId;
    this.getQuestionDetails(ExamSetId);
  },
  computed: {
    practiceList() {
      //排序之后的数组
      return this.questionList.map((item, index) => {
        const Options = item.Options.sort((a, b) => {
          return a.OrderIndex - b.OrderIndex;
        });
        return { ...item, Options };
      });
    }
  },
  methods: {
    /**请求题目数据
     * ExamId: 作业加密ID
     */
    async getQuestionDetails(ExamId) {
      const _this = this;
      let param = {
        ExamSetId: ExamId
      };
      await getQuestionDetails(param).then(res => {
        if (res != null) {
          const count = res.Questions.length;
          // 初始化提交答案的数据
          let list = new Array(count);
          for (var i = 0; i < list.length; i++) {
            list[i] = {
              QuestionId: "",
              Answer: ""
            };
            if (i == 0) {
              _this.currentQuestion = list[0];
            }
          }
          this.answerList = list;
          _this.questionList = res.Questions;
        }
      });
    },

    /**提交答案 */
    async submitExamSet(list) {
      const _this = this;
      const ExamSetId = this.$route.query.ExamSetId;
      let param = {
        ExamSetId: ExamSetId,
        QuestionList: list
      };
      await submitExamSet(param).then(res => {
        if (res != null) {
          this.$vux.toast.show({
            type: "success",
            text: "答案提交成功"
          });
          this.$router.push({
            path: `/${this.$store.state.BasePath}/Student/Homework`
          });
        }
      });
    },

    /**抽屉的状态切换 */
    drawerChange() {
      this.drawerVisibility = !this.drawerVisibility;
    },

    /**滑动之后的题目编号 */
    pageChange(currentIndex) {
      this.currentNum = currentIndex;
      this.drawerVisibility = false;
    },

    /**选择题目选项
     *  questionId:题目Id
     *  option:所选选项
     *  Order:当前题目序号
     */
    selectAnswer(questionId, option, Order) {
      this.answerList[Order - 1].QuestionId = questionId;
      this.answerList[Order - 1].Answer = option;
      setTimeout(() => {
        // 当前状态下停留1s后跳转到下一题
        if (Order < this.practiceList.length) this.currentNum = Order + 1;
      }, 1000);
    },

    /**提交的数据参数 */
    paramSet(){
      return this.answerList.filter((answer, index) => {
        if(answer.QuestionId){
          return answer;
        }
      })
    },

    /**校验答案 */
    submitAnswer() {
      let status = false;
      for (let i = 0; i < this.answerList.length; i++) {
        if (!this.answerList[i].QuestionId) {
          status = true;
          break;
        }
      }
      if (status) {
        this.confirmStatus = true;
      }else{
        const list = this.paramSet();
        this.submitExamSet(list);
      }
    },

    /**点击取消 */
    onCancel() {
      this.confirmStatus = false;
    },

    /**点击确定 */
    onConfirm() {
      this.confirmStatus = false;
      const list = this.paramSet();
      this.submitExamSet(list);
    }
  },
  components: {
    Icon,
    Flexbox,
    FlexboxItem,
    XButton,
    Toast,
    Popup,
    Confirm
  }
};
</script>

<style lang="less" scoped>
.vux-slider {
  height: 100%;
  font-size: 14px;
  color: #333;
}
.topDetail {
  line-height: 40px;
  font-size: 14px;
  .fright {
    text-align: right;
    margin-right: 10px;
  }
  .title {
    margin-left: 15px;
    color: #4a84f5;
  }
  .currentNum {
    color: #0099ff;
  }
}
.swiperItem {
  background: #fff;
  .questionTitle {
    padding: 20px 10px;
    line-height: 36px;
    img {
      max-width: 100%;
    }
  }
  .divider {
    width: 100%;
    height: 15px;
    background: #fbf9fe;
  }
  .optionSet {
    padding: 10px 20px;
    div {
      padding-bottom: 15px;
      .letter {
        width: 24px;
        line-height: 24px;
        text-align: center;
        border: 1px solid #ccc;
        border-radius: 50%;
        margin-right: 10px;
        display: inline-block;
      }
      .active {
        background: #3f87fe;
        color: #fff;
      }
    }
    .submitCls {
      margin-top: 20px;
      touch-action: none;
    }
    img {
      max-width: 100%;
    }
  }
}

.PopupSlot {
  padding: 15px 0 15px 6px;
  width: 156px;
  .optionter {
    float: left;
    font-size: 14px;
    width: 30px;
    line-height: 30px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 50%;
    margin: 9px;
  }
  .active {
    background: #3f87fe;
    color: #fff;
  }
  &:after {
    content: "";
    clear: both;
    display: block;
  }
}
</style>
