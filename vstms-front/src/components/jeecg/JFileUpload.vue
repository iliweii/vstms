<template>
  <span>
    <!-- 按钮和列表展示部分 -->
    <a-row :gutter="24">
      <a-col :md="24" :sm="24">
        <a-form-item :label="label" :label-col="labelColumn" :wrapper-col="wrapperColumn">
          <!-- 按钮 -->
          <a-button @click="() => this.open()" type="primary" :disabled="disabled">{{ text }}</a-button>
          <span style="color: red; display: inline-block; margin-left: 10px">{{ fileText }}</span>
          <div :style="{ marginTop: '10px' }"></div>
          <!-- 列表 -->
          <a-table
            ref="table"
            bordered
            size="middle"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
            :scroll="{ x: 600 }"
            :rowClassName="changeRowColor"
            @change="handleTableChange"
          >
            <span slot="action" slot-scope="text, record">
              <a @click="handleWatch(record)" :disabled="!previewable(record.fileName)">预览</a>
              <a-divider type="vertical" />
              <a @click="handleDownload(record)">下载</a>
              <a-divider type="vertical" v-if="record.createBy == userInfo().username" />
              <a @click="handleDelete(record.id)" v-if="record.createBy == userInfo().username" :disabled="disabled"
                >删除</a
              >
              <span v-for="op in extra" :key="op.emit">
                <a-divider type="vertical" />
                <a @click="() => $emit(op.emit, record)">{{ op.name }}</a>
              </span>
            </span>
          </a-table>
        </a-form-item>
      </a-col>
    </a-row>

    <!-- 附件上传表单模态框部分 -->
    <a-modal
      :title="title"
      :width="600"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      okText="确定"
    >
      <a-spin :spinning="confirmLoading">
        <!-- 表单 -->
        <a-form :form="form" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
          <a-form-item prop="fileType" required label="附件分类">
            <j-search-select-tag
              v-decorator="['fileType', validatorRules.fileType]"
              :triggerChange="true"
              placeholder="请选择附件分类"
              :dict="dict"
              :disabled="false"
            />
          </a-form-item>
          <!-- <a-form-item prop="fileAlias" required label="附件别名">
            <a-input
              placeholder="请输入附件别名"
              v-decorator="['fileAlias', validatorRules.fileAlias]"
              :triggerChange="true"
            />
          </a-form-item> -->
          <a-form-item prop="file" required label="上传附件" validate-status="error" help="">
            <j-upload
              style="width: 100%"
              :returnUrl="false"
              v-decorator="['file', validatorRules.file]"
              :fileType="fileType"
              :params="{ biz: 'temp' }"
              @change="fileChange"
            ></j-upload>
          </a-form-item>
        </a-form>
      </a-spin>
      <!-- 底部操作栏，去掉取消按钮 -->
      <template slot="footer">
        <a-button key="back" v-show="false"> 取消 </a-button>
        <a-button type="primary" key="submit" v-show="true" @click="handleOk"> 确认 </a-button>
      </template>
    </a-modal>

    <!-- 图片预览模态框部分 -->
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel()" :width="1200">
      <img alt="图片预览" style="width: 100%" :src="previewImage" />
    </a-modal>

    <!-- 视频预览模态框部分 -->
    <a-modal :visible="previewVideoVisible" :footer="null" @cancel="handleCancel()" :width="1200">
      <video width="100%" controls>
        <source :src="previewVideo" type="video/mp4" />
        <source :src="previewVideo" type="video/ogg" />
        <source :src="previewVideo" type="video/webm" />
      </video>
    </a-modal>
  </span>
</template>

<script>
/**
 * 上传附件使用方法样例：
 * <j-file-upload
 *   dict="file_default_type"
 *   businessType="业务类型（业务表名）"
 *   objectId="业务主键"
 * />
 * 除此之外，属性还包括title(模态框标题) text(链接文字) label(字段标题) labelColumn、wrapperColumn
 *
 */
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { ajaxGetDictItems } from '@/api/api'
import { getAction, putAction, deleteAction, downloadFile } from '@/api/manage'
import { mapGetters } from 'vuex'
import JUpload from '@/components/jeecg/JUpload'
import pick from 'lodash.pick'

export default {
  name: 'JFileUpload',
  mixins: [JeecgListMixin],
  components: {
    JUpload,
  },
  props: {
    dict: {
      type: String,
      default: 'file_default_type',
      required: false,
    },
    // 字典默认值
    dictDefault: {
      type: String,
      default: '',
      required: false,
    },
    title: {
      type: String,
      default: '附件上传',
      required: false,
    },
    label: {
      type: String,
      default: '附件',
      required: false,
    },
    text: {
      type: String,
      default: '添加',
      required: false,
    },
    labelColumn: {
      type: Object,
      default: () => ({ span: 3 }),
      required: false,
    },
    wrapperColumn: {
      type: Object,
      default: () => ({ span: 20 }),
      required: false,
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false,
    },
    extra: {
      type: Array,
      required: false,
      default: () => [],
    },
    fileType: {
      type: String,
      default: 'file',
      required: false,
    },
    businessType: String,
    objectId: String,
  },
  data() {
    return {
      confirmLoading: false,
      visible: false,
      fileText: '',
      // 图片预览
      previewImage: '',
      previewVisible: false,
      // 视频预览
      previewVideo: '',
      previewVideoVisible: false,
      form: this.$form.createForm(this),
      // 文件id列表数组，仅用于外部组件取值，在保存的时候一起提交数据到相应接口
      // 接口调用修改文件方法，实现文件与业务主键的绑定
      // this.$refs.XXXX(JFileUpload).fileIds
      fileIds: [],
      // 临时业务id
      tempObjectId: new Date().getTime(),
      validatorRules: {
        fileType: { rules: [{ required: true, message: '请选择附件分类!' }] },
        fileAlias: { rules: [{ required: true, message: '请输入附件别名!' }] },
        file: { rules: [{ required: true, message: '请点击上传附件!' }] },
      },
      columns: [
        {
          dataIndex: '',
          title: '序号',
          width: '10%',
          align: 'center',
          customRender: (t, r, index) => {
            return (this.ipagination.current - 1) * this.ipagination.pageSize + parseInt(index) + 1
          },
        },
        {
          dataIndex: 'fileTypeName',
          title: '附件类型',
          align: 'center',
          ellipsis: true,
          width: '10%',
        },
        {
          dataIndex: 'fileAlias',
          title: '附件名称',
          align: 'left',
          ellipsis: true,
          width: '40%',
        },
        {
          dataIndex: 'createBy_dictText',
          title: '上传人',
          align: 'center',
          ellipsis: true,
          width: '10%',
        },
        {
          dataIndex: 'createTime',
          title: '上传时间',
          align: 'center',
          ellipsis: true,
          width: '13%',
        },
        {
          title: '操作',
          dataIndex: 'action',
          fixed: 'right',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 240,
        },
      ],
      url: {
        list: '/sys/uploadFile/list',
        edit: '/sys/uploadFile/editFile',
        delete: '/sys/uploadFile/delete',
      },
    }
  },
  created() {
    this.loadData(1)
  },
  // 监控业务主键，变化时刷新列表数据
  watch: {
    objectId(val, oldVal) {
      this.loadData(1)
    },
  },
  methods: {
    ...mapGetters(['nickname', 'avatar', 'userInfo']),
    // 弹出上传附件模态框
    open() {
      if (this.dict == 'file_default_type') {
        setTimeout(() => {
          this.setForm({ fileType: '1' })
        }, 100)
      } else if (this.dictDefault != '') {
        setTimeout(() => {
          this.setForm({ fileType: this.dictDefault })
        }, 400)
      }
      this.visible = true
    },
    // 刷新列表数据
    loadData(arg) {
      if (arg === 1) {
        this.ipagination.current = 1
      }
      let params = this.getQueryParams()
      params.businessType = this.businessType
      params.objectId = this.objectId || this.tempObjectId
      params.dictCode = this.dict
      this.loading = true
      getAction(this.url.list, params).then((res) => {
        if (res.success && res.result) {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
          this.fileIds = this.dataSource.map((e) => e.id)
        }
        this.loading = false
      })
    },
    // 模态框填充数据
    setForm(data) {
      let that = this
      that.$nextTick(() => {
        that.form.setFieldsValue(pick(data, 'fileType', 'fileAlias'))
      })
    },
    // 数据回写（修改）
    writeback(record) {
      putAction(this.url.edit, record).then((res) => {
        if (res.success) {
          this.loadData(1)
        }
      })
    },
    // 模态框确认事件（执行回写和关闭）
    handleOk() {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.writeback({
            ...values,
          })
          this.close()
        }
      })
    },
    // 模态框取消事件
    handleCancel() {
      this.close()
      this.previewVisible = false
      this.previewVideoVisible = false
    },
    // 文件预览（图片、PDF）
    handleWatch: function (record) {
      let filePath = window._CONFIG['staticDomainURL'] + '/' + record.filePath
      let index = filePath.lastIndexOf('.')
      let ext = filePath.substr(index + 1).toLowerCase()
      if (this.isAssetTypeAnImage(ext)) {
        this.handlePreview(filePath)
      } else if (ext === 'pdf') {
        window.open(window._CONFIG['previewFileUrl'] + filePath)
      } else if (this.isAssetTypeAVideo(ext)) {
        this.handlePreviewVideo(filePath)
      } else {
        this.handleDownload(record)
      }
    },
    // 图片的预览
    handlePreview(filePath) {
      this.previewImage = filePath
      this.previewVisible = true
    },
    // 视频的预览
    handlePreviewVideo(filePath) {
      this.previewVideo = filePath
      this.previewVideoVisible = true
    },
    // 文件下载
    handleDownload: function (record) {
      let url = window._CONFIG['staticDomainURL'] + '/' + record.filePath
      let filename = record.fileAlias + record.fileName.substr(record.fileName.lastIndexOf('.'))
      downloadFile(url, filename, {})
    },
    // 文件删除
    handleDelete(id) {
      let that = this
      that.$confirm({
        title: '确认删除',
        content: '确定要删除本附件吗?',
        onOk: function () {
          deleteAction(that.url.delete, {
            id: id,
          }).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.loadData()
              that.$emit('delete')
            } else {
              that.$message.warning(res.message)
            }
          })
        },
      })
    },
    // 关闭事件
    close() {
      this.$emit('close')
      this.visible = false
      this.form.resetFields()
    },
    // 文件列表变化事件
    fileChange(file) {
      this.loadData(1)
      if (file.length < 1) {
        return
      }
      file.forEach((e) => {
        e.fileAlias = e.fileName.substring(0, e.fileName.indexOf('.'))
      })
      this.$message.success(`操作成功!`)
      this.writeback({
        file: file,
        fileType: this.form.getFieldValue('fileType'),
        businessType: this.businessType,
        objectId: this.objectId || this.tempObjectId,
      })
    },
    // 获取文件类型字典
    getFileType() {
      ajaxGetDictItems(this.dict)
    },
    // 是否可预览（非图片和pdf暂时不允许预览）
    previewable(name) {
      let index = name.lastIndexOf('.')
      let ext = name.substr(index + 1).toLowerCase()
      return this.isAssetTypeAnImage(ext) || this.isAssetTypeAVideo(ext) || ext === 'pdf'
    },
    // 判断后缀是否为图片
    isAssetTypeAnImage(ext) {
      return ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1
    },
    // 判断后缀是否为视频
    isAssetTypeAVideo(ext) {
      return ['mp4', 'avi', 'rmvb', 'wmv', 'mkv', 'flv', 'mov', '3gp', 'mpg', 'mpeg'].indexOf(ext.toLowerCase()) !== -1
    },
  },
}
</script>

<style scoped lang="less"></style>
