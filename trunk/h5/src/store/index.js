import Vue from 'vue';
import Vuex from 'vuex';
import { getSessionStorage, setSessionStorage } from '../utils/storage';
import { getUserBaseInfo } from "../services/getData";

Vue.use(Vuex);

/**
 * 全局公用状态
 */
const store = new Vuex.Store({
    state: {
		/**
		 * 当前用户系统，1：线上；2：线下
		 */
        CurUserSystem: 1,
        /**
         * 系统当前路径
         */
        BasePath: 'Online',
        /**
         * 用户基础信息
         */
        userInfo: null
    },
    mutations: {
        /**
         * 更新用户信息state
         * @param {} state 
         * @param {*} userInfo 
         */
        updateUsrInfo(state, userInfo) {
            state.userInfo = userInfo;
        },
        /**
         * 更新当前系统标识
         * @param {*} state 
         * @param {*} curUserSystem 
         */
        updateSystem(state, curUserSystem) {
            state.CurUserSystem = curUserSystem;
            if (curUserSystem === 1) {
                state.BasePath = 'Online';
            } else {
                state.BasePath = 'Offline';
            }
        }
    },
    actions: {
        /** 
         * 获取用户信息
         */
        async getUserInfo(context, isRest) {
            //缓存数据
            let userBaseInfo = getSessionStorage('UserBaseInfo');
            //当前Model存储的状态
            let sessionInfo = context.state.userInfo;
            if (!userBaseInfo || isRest) {
                var result = await getUserBaseInfo();
                userBaseInfo = result;
                sessionInfo = null;
                setSessionStorage('UserBaseInfo', userBaseInfo);
            }
            if (!sessionInfo) {
                sessionInfo = userBaseInfo;
            }
            context.commit('updateUsrInfo', sessionInfo)
            return sessionInfo;
        }
    }
});
export default store;