package nl.renarj.jasdb.rest.serializers;

import nl.renarj.jasdb.rest.exceptions.RestException;
import nl.renarj.jasdb.rest.model.RestEntity;

import java.io.InputStream;
import java.io.OutputStream;

public interface RestResponseHandler {
	public <T extends RestEntity> T deserialize(Class<T> dataType, InputStream inputStream) throws RestException;
    public <T extends RestEntity> T deserialize(Class<T> dataType, String data) throws RestException;

	public void serialize(RestEntity entity, OutputStream outputStream) throws RestException;

	public String getMediaType();
}
