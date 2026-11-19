package org.h2.jmx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;
import org.h2.util.Utils;

/* loaded from: ijava.jar:org/h2/jmx/DocumentedMBean.class */
public class DocumentedMBean extends StandardMBean {
    private final String interfaceName;
    private Properties resources;

    public <T> DocumentedMBean(T t, Class<T> cls) throws NotCompliantMBeanException {
        super(t, cls);
        this.interfaceName = t.getClass().getName() + "MBean";
    }

    private Properties getResources() {
        if (this.resources == null) {
            this.resources = new Properties();
            try {
                byte[] resource = Utils.getResource("/org/h2/res/javadoc.properties");
                if (resource != null) {
                    this.resources.load(new ByteArrayInputStream(resource));
                }
            } catch (IOException e) {
            }
        }
        return this.resources;
    }

    protected String getDescription(MBeanInfo mBeanInfo) {
        String property = getResources().getProperty(this.interfaceName);
        return property == null ? super.getDescription(mBeanInfo) : property;
    }

    protected String getDescription(MBeanOperationInfo mBeanOperationInfo) {
        String property = getResources().getProperty(this.interfaceName + "." + mBeanOperationInfo.getName());
        return property == null ? super.getDescription(mBeanOperationInfo) : property;
    }

    protected String getDescription(MBeanAttributeInfo mBeanAttributeInfo) {
        String property = getResources().getProperty(this.interfaceName + "." + (mBeanAttributeInfo.isIs() ? "is" : "get") + mBeanAttributeInfo.getName());
        return property == null ? super.getDescription(mBeanAttributeInfo) : property;
    }

    protected int getImpact(MBeanOperationInfo mBeanOperationInfo) {
        if (mBeanOperationInfo.getName().startsWith("list")) {
            return 0;
        }
        return 1;
    }
}
