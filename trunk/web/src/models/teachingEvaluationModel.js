import * as services from '../services/teachingEvaluationServices';

export default {
	namespace: 'teachModel',
	state: {
	},

	reducers: {
	},

	effects: {
        // 获取主讲老师的所有可评价的课程
        *getAllSubjectEvaluate({ payload }, { call }) {
            return yield call(services.getAllSubjectEvaluate, payload)
        },
        // 主讲获取课程评价
        *getSubjectEvaluate({ payload }, { call }) {
            return yield call(services.getSubjectEvaluate, payload)
        },
        //消息管理列表
        *getMessageList({ payload }, { call }) {
            return yield call(services.getMessageList, payload);
        },
        //回复消息
        *replayMessage({ payload }, { call }) {
            return yield call(services.replayMessage, payload);
        },
        //助教 - 课程评价列表
        *getSubjectEvaluateList({ payload }, { call }) {
            return yield call(services.getSubjectEvaluateList, payload);
        },
        //助教 -评价课程
        *evaluateSubject({ payload }, { call }) {
            return yield call(services.evaluateSubject, payload);
        }
	},
	subscriptions: {
	}
};
