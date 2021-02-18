import * as services from '../services/studentPersonalServices';

export default {
    namespace: 'studentPersonalModel',
    state: {
    },

    reducers: {
    },

    effects: {

        //修改个人信息
        *EditStudentInfo({ payload }, { call }) {
            return yield call(services.EditStudentInfo, payload);
        },

        //修改密码
        *ChangePassword({ payload }, { call }) {
            return yield call(services.ChangePassword, payload);
        },

        //修改手机
        *ChangePhoneNum({ payload }, { call }) {
            return yield call(services.ChangePhoneNum, payload);
        },

        //修改邮箱
        *ChangeEmail({ payload }, { call }) {
            return yield call(services.ChangeEmail, payload);
        },
        /**
		 * 获取个人信息
		 * @param {*} param0 
		 * @param {*} param1 
		 */
		*GetUserInfo({payload}, { call,put,all }) {
               
            return yield call(services.GetUserInfo, payload);
		},

    },
    subscriptions: {
    }
};
