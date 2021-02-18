import './index.less';
import moment from "moment";
import "moment/locale/zh-cn";
import { createLogger } from 'redux-logger';
moment.locale("zh-cn");

export const dva = {
  config: {
    onError(e) {
      e.preventDefault();
      console.error(e.message);
    },
    // onAction: createLogger()
  },
  plugins: [
  ],
};                                                                        