<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="跟踪编码">
                <j-input placeholder="请输入跟踪编码" v-model="queryParam.no"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="跟踪名称">
                <j-input placeholder="请输入跟踪名称" v-model="queryParam.title"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="跟踪状态">
                <j-search-select-tag
                  v-model="queryParam.status"
                  :triggerChange="true"
                  placeholder="请选择跟踪状态"
                  dict="tracking_status"
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
          </a-row>

          <template v-if="toggleSearchStatus"> </template>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('训后跟踪')">导出</a-button>
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
          <span slot="status" slot-scope="text, record">
            <a-alert v-if="record.status == 0" :message="text" type="warning" />
            <a-alert v-else-if="record.status == 5" :message="text" type="error" />
            <a-alert v-else :message="text" type="success" />
          </span>
          <span slot="action" slot-scope="text, record">
            <a v-if="record.status == 0" @click="handleEdit(record)">编辑</a>
            <a v-else @click="handleDetail(record)">详情</a>

            <a-divider type="vertical" />
            <a :disabled="record.status == 0" @click="handleBaseDetail(record)">训后跟踪</a>

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
                <a-menu-item v-if="record.status > 0 && record.status < 5">
                  <a-popconfirm title="确定禁用跟踪吗?" @confirm="() => handleDisable(record.id)">
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
      <tracking-modal ref="modalForm" @ok="modalFormOk"></tracking-modal>
      <tracking-base-modal ref="trackingBaseModal" @ok="modalFormOk"></tracking-base-modal>
    </a-card>
  </page-layout>
</template>

<script>
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrackingModal from './modules/TrackingModal'
import TrackingBaseModal from './modules/TrackingBaseModal'
import { putAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TrackingList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrackingModal,
    TrackingBaseModal,
  },
  data() {
    return {
      description: '训后跟踪管理页面',
      title: '训后跟踪',
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
          title: '跟踪编码',
          align: 'center',
          dataIndex: 'no',
          ellipsis: true,
          width: 220,
        },
        {
          title: '跟踪名称',
          align: 'center',
          dataIndex: 'title',
          ellipsis: true,
        },
        {
          title: '跟踪状态',
          align: 'center',
          dataIndex: 'status_dictText',
          scopedSlots: { customRender: 'status' },
          ellipsis: true,
          width: 120,
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText',
          ellipsis: true,
          width: 120,
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
          sorter: true,
          ellipsis: true,
          width: 120,
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
          width: 240,
        },
      ],
      url: {
        list: '/tracking/trackingProgram/list',
        edit: '/tracking/trackingProgram/edit',
        delete: '/tracking/trackingProgram/delete',
        deleteBatch: '/tracking/trackingProgram/deleteBatch',
        exportXlsUrl: 'tracking/trackingProgramexportXls',
        importExcelUrl: 'tracking/trackingProgramimportExcel',
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
      putAction(that.url.edit, { id, status: 5 }).then((res) => {
        that.$message.success('禁用成功')
        that.loadData()
      })
    },
    handleBaseDetail(record) {
      this.$refs.trackingBaseModal.edit(record)
      this.$refs.trackingBaseModal.title = '训后跟踪'
      this.$refs.trackingBaseModal.disableSubmit = true
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>