<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="编号">
                <j-input placeholder="请输入编号" v-model="queryParam.no"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="名称">
                <j-input placeholder="请输入名称" v-model="queryParam.name"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-search-select-tag
                  v-model="queryParam.status"
                  :triggerChange="true"
                  placeholder="请选择状态"
                  dict="training_class_status"
                  :disabled="false"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                </a>
              </span>
            </a-col>

            <template v-if="toggleSearchStatus"> </template>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('培训班')">导出</a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
        </a-dropdown>
      </div>

      <!-- table区域-begin -->
      <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
          >项
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          class="j-table-force-nowrap"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          @change="handleTableChange"
        >
          <span
            slot="teacher"
            slot-scope="text, record"
            :title="`培训班实际教师${record.teacherRealNum}人，预估${text}人`"
          >
            {{ record.teacherRealNum }}人 ({{ text }}人)
          </span>
          <span
            slot="student"
            slot-scope="text, record"
            :title="`培训班实际学生${record.studentRealNum}人，预估${text}人`"
          >
            {{ record.studentRealNum }}人 ({{ text }}人)
          </span>
          <span slot="action" slot-scope="text, record">
            <a v-if="record.status == 0" @click="handleEdit(record)">编辑</a>
            <a v-else @click="handleDetail(record)">详情</a>

            <a-divider type="vertical" />
            <a :disabled="record.status == 0" @click="handleBaseDetail(record)">信息录入</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item v-if="record.status == 0">
                  <a @click="handleSubmit(record.id)">提交</a>
                </a-menu-item>
                <a-menu-item v-if="record.status == 0">
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
                <a-menu-item v-if="record.status > 0 && record.status < 3">
                  <a-popconfirm title="确定禁用培训班吗?" @confirm="() => handleDisable(record.id)">
                    <a>禁用</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <trainingClass-modal ref="modalForm" @ok="modalFormOk"></trainingClass-modal>
      <trainingClass-base-modal ref="modalBaseForm" @ok="modalFormOk"></trainingClass-base-modal>
    </a-card>
  </page-layout>
</template>

<script>
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrainingClassModal from './modules/TrainingClassModal'
import TrainingClassBaseModal from './modules/TrainingClassBaseModal.vue'
import { putAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TrainingClassList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrainingClassModal,
    TrainingClassBaseModal,
  },
  data() {
    return {
      description: '培训班管理页面',
      title: '培训班基本信息维护',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '编号',
          align: 'center',
          dataIndex: 'no',
        },
        {
          title: '名称',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '职业技能名称',
          align: 'center',
          dataIndex: 'skillName',
        },
        {
          title: '课时',
          align: 'center',
          dataIndex: 'period',
        },
        {
          title: '价格(元)',
          align: 'center',
          dataIndex: 'price',
        },
        {
          title: '教师数量',
          align: 'center',
          dataIndex: 'teacherNum',
          scopedSlots: { customRender: 'teacher' },
        },
        {
          title: '学生数量',
          align: 'center',
          dataIndex: 'studentNum',
          scopedSlots: { customRender: 'student' },
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status_dictText',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/training/trainingClass/list',
        edit: '/training/trainingClass/edit',
        delete: '/training/trainingClass/delete',
        deleteBatch: '/training/trainingClass/deleteBatch',
        exportXlsUrl: 'training/trainingClass/exportXls',
        importExcelUrl: 'training/trainingClass/importExcel',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    handleSubmit(id) {
      const that = this
      putAction(that.url.edit, { id, status: 1 }).then((res) => {
        that.$message.success('提交成功')
        that.loadData()
      })
    },
    handleDisable(id) {
      const that = this
      putAction(that.url.edit, { id, status: 3 }).then((res) => {
        that.$message.success('禁用成功')
        that.loadData()
      })
    },
    handleBaseDetail(record) {
      this.$refs.modalBaseForm.edit(record)
      this.$refs.modalBaseForm.title = '培训班信息录入'
      this.$refs.modalBaseForm.disableSubmit = true
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>