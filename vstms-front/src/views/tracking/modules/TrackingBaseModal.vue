<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    :destroyOnClose="true"
    placement="right"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    style="height: 100%; overflow: auto; padding-bottom: 53px"
  >
    <template slot="title">
      <div style="width: 100%; height: 30px">
        <span>{{ title }}</span>
        <span style="display: inline-block; width: calc(100% - 51px); padding-right: 10px; text-align: right">
          <a-button @click="toggleScreen" icon="appstore" style="height: 20px; width: 20px; border: 0px"></a-button>
        </span>
      </div>
    </template>

    <a-alert :message="object.title" type="success" />
    <a-row :gutter="24" :style="{ marginTop: '5px' }">
      <a-col :md="8">
        <span :style="{ padding: '15px' }">创建人：{{ object.createBy_dictText }}</span>
      </a-col>
      <a-col :md="8">
        <span :style="{ padding: '15px' }">创建时间：{{ object.createTime }}</span>
      </a-col>
      <a-col :md="8">
        <span :style="{ padding: '15px' }">当前状态：{{ object.status_dictText }}</span>
      </a-col>
    </a-row>

    <!-- 标签页 -->
    <a-tabs :activeKey="activeKey" @tabClick="tabClick">
      <a-tab-pane key="1" tab="跟踪方案">
        <tracking-plan-list
          :object="object"
          :disabled="object.status != '1'"
          :key="key + 1"
          @close="handleCancel"
          @change="handleChange"
        />
      </a-tab-pane>
      <a-tab-pane key="2" tab="跟踪过程">
        <tracking-process
          :object="object"
          :disabled="object.status != '2'"
          :key="key + 2"
          @close="handleCancel"
          @change="handleChange"
        />
      </a-tab-pane>
      <a-tab-pane key="3" tab="跟踪总结">
        <tracking-report-list
          :object="object"
          :disabled="object.status != '3'"
          :key="key + 3"
          @close="handleCancel"
          @change="handleChange"
        />
      </a-tab-pane>
    </a-tabs>
  </a-drawer>
</template>

<script>
import { getDictItemsFromCache } from '@/api/api'
import TrackingPlanList from './TrackingPlanList.vue' // 跟踪方案
import TrackingProcess from './TrackingProcess.vue' // 跟踪过程
import TrackingReportList from './TrackingReportList.vue' // 跟踪过程

export default {
  name: 'TrackingBaseModal',
  components: {
    TrackingPlanList,
    TrackingProcess,
    TrackingReportList,
  },
  data() {
    return {
      drawerWidth: '70%',
      title: '操作',
      visible: false,
      disableSubmit: false,
      activeKey: '1',
      key: String(new Date()),
      object: {}, // 对象
    }
  },
  methods: {
    // 窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth
      } else {
        this.modalWidth = 800
      }
      this.modaltoggleFlag = !this.modaltoggleFlag
    },
    /**
     * Tab页签切换事件
     */
    tabClick(params) {
      this.activeKey = params
    },
    async add() {
      this.visible = true
    },
    async edit(record) {
      this.visible = true
      record.description = null
      this.object = record
    },
    handleCancel() {
      this.object = {}
      this.visible = false
      this.$emit('ok')
    },
    handleChange(object) {
      getDictItemsFromCache('tracking_status')
        .filter((x) => x.value === object.status)
        .forEach((x) => {
          object.status_dictText = x.text
        })
      this.object = object
      // 刷新key
      this.key = String(new Date())
    },
  },
}
</script>

<style scoped lang="less">
.top_button {
  padding-bottom: 10px;
  .ant-btn {
    margin-right: 10px;
  }
}

/deep/.ant-modal-footer {
  text-align: center;
}
/deep/.ant-col .ant-form-item-label {
  display: inline-block;
  text-align: right;
}
/deep/.table-page-search-wrapper .ant-form-inline .ant-form-item {
  margin-bottom: 0px;
}
/deep/.ant-form-item-label {
  text-align: center;
}
/deep/.ant-row .ant-form-item {
  margin-bottom: 0px;
}
/deep/.ant-row .ant-form-item .ant-form-explain {
  position: absolute;
}
/deep/.ant-form-item-label,
/deep/.ant-form-item-control {
  line-height: 35px;
}
/deep/.ant-btn > .anticon + span,
.ant-btn > span + .anticon {
  margin-left: 0;
}
.drawer-bootom-button {
  position: absolute;
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}
</style>
