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

    <a-alert type="success">
      <span slot="message">
        <span>{{ object.name }}</span>
        <a-button
          @click="() => handleSubmit(object.id)"
          type="primary"
          :disabled="object.status != '1'"
          icon="check"
          :style="{ marginLeft: '40%' }"
          >完成信息录入</a-button
        >
      </span>
    </a-alert>

    <!-- 标签页 -->
    <a-tabs :activeKey="activeKey" @tabClick="tabClick">
      <a-tab-pane key="1" tab="教师录入">
        <training-class-teacher-list
          :object="object"
          :disabled="object.status != '1'"
          :key="key + 1"
          @close="handleCancel"
          @change="handleChange"
        />
      </a-tab-pane>
      <a-tab-pane key="2" tab="学生录入">
        <training-class-student-list
          :object="object"
          :disabled="object.status != '1'"
          :key="key + 2"
          @close="handleCancel"
          @change="handleChange"
        />
      </a-tab-pane>
    </a-tabs>
  </a-drawer>
</template>

<script>
import { putAction } from '@/api/manage'
import TrainingClassStudentList from './TrainingClassStudentList.vue' // 学生列表
import TrainingClassTeacherList from './TrainingClassTeacherList.vue' // 教师列表

export default {
  name: 'TrainingClassBaseModal',
  components: {
    TrainingClassStudentList,
    TrainingClassTeacherList,
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
      url: {
        edit: '/training/trainingClass/edit',
      },
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
      this.object = object
      // 刷新key
      this.key = String(new Date())
    },
    handleSubmit(id) {
      const that = this
      putAction(that.url.edit, { id, status: 2 }).then((res) => {
        that.$message.success('提交成功')
        that.object.status = '2'
        that.handleCancel()
      })
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
