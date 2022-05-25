<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="总结编号">
              <j-search-select-tag
                v-model="queryParam.no"
                :triggerChange="true"
                placeholder="请选择培训班"
                dict="training_class,name,no"
                :disabled="false"
                @change="loadData()"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="总结内容">
              <j-input placeholder="请输入总结内容" v-model="queryParam.content"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="总结类型">
              <j-search-select-tag
                v-model="queryParam.type"
                :triggerChange="true"
                placeholder="请选择总结类型"
                dict="training_summary_type"
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
      <a-button type="primary" icon="download" @click="handleExportXls('培训总结表')">导出</a-button>
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
        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">详情</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleEdit(record)">编辑</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <trainingSummary-modal ref="modalForm" @ok="modalFormOk"></trainingSummary-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import TrainingSummaryModal from './modules/TrainingSummaryModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TrainingSummaryList',
  mixins: [JeecgListMixin],
  components: {
    TrainingSummaryModal,
  },
  data() {
    return {
      description: '培训总结表管理页面',
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
          title: '总结编号',
          align: 'center',
          dataIndex: 'no',
          ellipsis: true,
          width: 200,
        },
        {
          title: '总结内容',
          align: 'center',
          dataIndex: 'content',
          ellipsis: true,
        },
        {
          title: '总结类型',
          align: 'center',
          dataIndex: 'type_dictText',
          ellipsis: true,
          sorter: (a, b) => a.type - b.type,
          width: 120,
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText',
          ellipsis: true,
          sorter: (a, b) => a.createBy - b.createBy,
          width: 120,
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
          sorter: (a, b) => a.createTime - b.createTime,
          width: 140,
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
          width: 120,
        },
      ],
      url: {
        list: '/training/trainingSummary/list',
        delete: '/training/trainingSummary/delete',
        deleteBatch: '/training/trainingSummary/deleteBatch',
        exportXlsUrl: 'training/trainingSummary/exportXls',
        importExcelUrl: 'training/trainingSummary/importExcel',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {},
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>