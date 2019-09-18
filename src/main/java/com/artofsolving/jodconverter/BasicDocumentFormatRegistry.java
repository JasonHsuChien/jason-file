 /**
     *其中不支持docx解析，重写源码
     *注意新建同名的包com.artofsolving.jodconverter，类名保持一致ok
     */

package com.artofsolving.jodconverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicDocumentFormatRegistry implements DocumentFormatRegistry {
    private List<DocumentFormat> documentFormats = new ArrayList<DocumentFormat>();
    public BasicDocumentFormatRegistry() {
    }
    public void addDocumentFormat(DocumentFormat documentFormat) {
        this.documentFormats.add( documentFormat );
    }
    protected List<DocumentFormat> getDocumentFormats() {
        return this.documentFormats;
    }
    @Override
    public DocumentFormat getFormatByFileExtension(String extension) {
        if (extension == null) {
            return null;
        } else {
            if (extension.indexOf( "doc" ) >= 0) {
                extension = "doc";
            }
            if (extension.indexOf( "ppt" ) >= 0) {
                extension = "ppt";
            }
            if (extension.indexOf( "xls" ) >= 0) {
                extension = "xls";
            }
            String lowerExtension = extension.toLowerCase();
            Iterator<DocumentFormat> it = this.documentFormats.iterator();
            DocumentFormat format;
            do {
                if (!it.hasNext()) {
                    return null;
                }
                format = (DocumentFormat) it.next();
            } while (!format.getFileExtension().equals( lowerExtension ));

            return format;
        }
    }
    @Override
    public DocumentFormat getFormatByMimeType(String mimeType) {
        Iterator<DocumentFormat> it = this.documentFormats.iterator();
        DocumentFormat format;
        do {
            if (!it.hasNext()) {
                return null;
            }
            format = (DocumentFormat) it.next();
        } while (!format.getMimeType().equals( mimeType ));
        return format;
    }
}