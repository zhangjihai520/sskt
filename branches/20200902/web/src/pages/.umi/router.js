import React from 'react';
import { Router as DefaultRouter, Route, Switch } from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/_renderRoutes';
import RendererWrapper0 from 'E:/project/sskt/trunk/web/src/pages/.umi/LocaleWrapper.jsx'

let Router = require('dva/router').routerRedux.ConnectedRouter;

let routes = [
  {
    "path": "/index.html",
    "component": require('../NoLogin/MinLesson/index.js').default,
    "exact": true
  },
  {
    "path": "/",
    "component": require('../NoLogin/MinLesson/index.js').default,
    "exact": true
  },
  {
    "path": "/AdminOnline",
    "component": require('../Admin/AdminIndex.js').default,
    "exact": true
  },
  {
    "path": "/AdminOffline",
    "component": require('../Admin/AdminIndex.js').default,
    "exact": true
  },
  {
    "path": "/AdminOnline/",
    "exact": false,
    "component": require('../../layouts/AdminLayout/index.js').default
  },
  {
    "path": "/AdminOffline/",
    "exact": false,
    "component": require('../../layouts/AdminLayout/index.js').default
  },
  {
    "path": "/TeaOnline",
    "component": require('../Teacher/TeacherIndex.js').default,
    "exact": true
  },
  {
    "path": "/TeaOffline",
    "component": require('../Teacher/TeacherIndex.js').default,
    "exact": true
  },
  {
    "path": "/TeaOnline/",
    "exact": false,
    "component": require('../../layouts/TeacherLayout/index.js').default
  },
  {
    "path": "/TeaOffline/",
    "exact": false,
    "component": require('../../layouts/TeacherLayout/index.js').default
  },
  {
    "path": "/StudentOnline",
    "exact": false,
    "component": require('../../layouts/StudentLayout/index.js').default
  },
  {
    "path": "/StudentOffline",
    "exact": false,
    "component": require('../../layouts/StudentLayout/index.js').default
  },
  {
    "path": "/AccessOnline/Login",
    "component": require('../Access/Login.js').default,
    "exact": true
  },
  {
    "path": "/AccessOnline/Logout",
    "component": require('../Access/Logout.js').default,
    "exact": true
  },
  {
    "path": "/AccessOffline/Login",
    "component": require('../Access/Login.js').default,
    "exact": true
  },
  {
    "path": "/AccessOffline/Logout",
    "component": require('../Access/Logout.js').default,
    "exact": true
  },
  {
    "path": "/Access/ExamSetRedirect",
    "component": require('../Access/ExamSetRedirect.js').default,
    "exact": true
  },
  {
    "path": "/AccessOnline/AntiVirusPublicClass",
    "component": require('../NoLogin/HomePage/antiVirus.js').default,
    "exact": true
  },
  {
    "path": "/AccessOnline/Home",
    "component": require('../NoLogin/HomePage/index.js').default,
    "exact": true
  },
  {
    "path": "/AccessOnline/MinLesson",
    "component": require('../NoLogin/MinLesson/index.js').default,
    "exact": true
  },
  {
    "component": () => React.createElement(require('E:/project/sskt/trunk/web/node_modules/umi-build-dev/lib/plugins/404/NotFound.js').default, { pagesPath: 'src/pages', hasRoutesInConfig: true })
  }
];
window.g_routes = routes;
window.g_plugins.applyForEach('patchRoutes', { initialValue: routes });

// route change handler
function routeChangeHandler(location, action) {
  window.g_plugins.applyForEach('onRouteChange', {
    initialValue: {
      routes,
      location,
      action,
    },
  });
}
window.g_history.listen(routeChangeHandler);
routeChangeHandler(window.g_history.location);

export default function RouterWrapper() {
  return (
<RendererWrapper0>
          <Router history={window.g_history}>
      { renderRoutes(routes, {}) }
    </Router>
        </RendererWrapper0>
  );
}
