package com.selecttvapp.RPC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public abstract class JSONRPCClient {
    protected int soTimeout = 0;
    protected int connectionTimeout = 0;

    public JSONRPCClient() {
    }

    public static JSONRPCClient create(String uri) {
        return new JSONRPCHttpClient(uri);
    }

    protected abstract JSONObject doJSONRequest(JSONObject var1) throws JSONRPCException;

    protected abstract JSONObject doJSONGetRequest(JSONObject var1) throws JSONRPCException;

    protected abstract JSONObject doJSONGetRequest() throws JSONRPCException;

    protected JSONObject doRequest(String method, Object[] params) throws JSONRPCException {
        JSONArray jsonParams = new JSONArray();

        for (int jsonRequest = 0; jsonRequest < params.length; ++jsonRequest) {
            jsonParams.put(params[jsonRequest]);
        }

        JSONObject var7 = new JSONObject();

        try {
            var7.put("id", UUID.randomUUID().hashCode());
            var7.put("method", method);
            var7.put("params", jsonParams);
        } catch (JSONException var6) {
            throw new JSONRPCException("Invalid JSON request", var6);
        }

        return this.doJSONRequest(var7);
    }

    protected JSONObject doRequest(String method, JSONObject params) throws JSONRPCException, JSONException {
        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("id", UUID.randomUUID().hashCode());
            jsonRequest.put("method", method);
            jsonRequest.put("params", params);
            jsonRequest.put("jsonrpc", "2.0");
        } catch (JSONException var5) {
            throw new JSONRPCException("Invalid JSON request", var5);
        }

        return this.doJSONRequest(jsonRequest);
    }

    protected JSONObject doRequest() throws JSONRPCException {
        JSONObject var7 = new JSONObject();

        try {
            var7.put("id", UUID.randomUUID().hashCode());
        } catch (JSONException var6) {
            throw new JSONRPCException("Invalid JSON request", var6);
        }

        return this.doJSONRequest(var7);
    }

    protected JSONObject doGetRequest() throws JSONRPCException {
        JSONObject var7 = new JSONObject();

        try {
            var7.put("id", UUID.randomUUID().hashCode());
        } catch (JSONException var6) {
            throw new JSONRPCException("Invalid JSON request", var6);
        }

        return this.doJSONGetRequest(var7);
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Object call(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).get("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result", var4);
        }
    }

    public Object call(String method, JSONObject params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).get("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to String", var4);
        }
    }

    public String callString(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getString("result");
        } catch (JSONRPCException var4) {
            throw new JSONRPCException("Cannot convert result to String", var4);
        } catch (JSONException var5) {
            throw new JSONRPCException("Cannot convert result to String", var5);
        }
    }

    public String callString(String method, JSONObject params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getString("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to String", var4);
        } catch (JSONRPCException var5) {
            throw new JSONRPCException("Cannot convert result to String", var5);
        } catch (Exception var6) {
            throw new JSONRPCException("Cannot convert result to String", var6);
        }
    }

    public int callInt(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getInt("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to int", var4);
        }
    }

    public Object callInt(String method, JSONObject params) throws JSONRPCException {
        JSONObject response = null;

        try {
            response = this.doRequest(method, params);
            return Integer.valueOf(response.getInt("result"));
        } catch (JSONException var8) {
            if (response == null) {
                throw new JSONRPCException("Cannot call method: " + method, var8);
            } else {
                try {
                    return Integer.valueOf(Integer.parseInt(response.getString("result")));
                } catch (NumberFormatException var6) {
                    throw new JSONRPCException("Cannot convert result to int", var6);
                } catch (JSONException var7) {
                    throw new JSONRPCException("Cannot convert result to int", var7);
                }
            }
        }
    }

    public long callLong(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getLong("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to long", var4);
        }
    }

    public long callLong(String method, JSONObject params) throws JSONRPCException {
        JSONObject response = null;

        try {
            response = this.doRequest(method, params);
            return response.getLong("result");
        } catch (JSONException var8) {
            if (response == null) {
                throw new JSONRPCException("Cannot call method: " + method, var8);
            } else {
                try {
                    return Long.parseLong(response.getString("result"));
                } catch (NumberFormatException var6) {
                    throw new JSONRPCException("Cannot convert result to long", var8);
                } catch (JSONException var7) {
                    throw new JSONRPCException("Cannot convert result to long", var8);
                }
            }
        }
    }

    public boolean callBoolean(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getBoolean("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to boolean", var4);
        }
    }

    public boolean callBoolean(String method, JSONObject params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getBoolean("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to boolean", var4);
        }
    }

    public double callDouble(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getDouble("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to double", var4);
        }
    }

    public double callDouble(String method, JSONObject params) throws JSONRPCException {
        JSONObject response = null;

        try {
            response = this.doRequest(method, params);
            return response.getDouble("result");
        } catch (JSONException var8) {
            JSONException e = var8;

            try {
                if (response == null) {
                    throw new JSONRPCException("Cannot call method: " + method, e);
                } else {
                    return Double.parseDouble(response.getString("result"));
                }
            } catch (NumberFormatException var6) {
                throw new JSONRPCException("Cannot convert result to double", var6);
            } catch (JSONException var7) {
                throw new JSONRPCException("Cannot convert result to double", var7);
            }
        }
    }

    public JSONObject callJSONObject(String method, JSONObject params) throws JSONRPCException {
        JSONObject response = null;

        try {
            response = this.doRequest(method, params);
            return response.getJSONObject("result");
        } catch (JSONException var7) {
            JSONException e = var7;

            try {
                if (response == null) {
                    throw new JSONRPCException("Cannot call method: " + method, e);
                } else {
                    return new JSONObject(response.getString("result"));
                }
            } catch (JSONException var6) {
                throw new JSONRPCException("Cannot convert result to JSONObject", var7);
            }
        }
    }

    public JSONObject callJSONObject(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getJSONObject("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to JSONObject", var4);
        }
    }

    public JSONArray callJSONArray(String method, Object... params) throws JSONRPCException {
        try {
            return this.doRequest(method, params).getJSONArray("result");
        } catch (JSONException var4) {
            throw new JSONRPCException("Cannot convert result to JSONArray", var4);
        }
    }

    public JSONObject callJSONObject() throws JSONRPCException {
        return this.doRequest();
    }

    public JSONObject callJSONGetObject() throws JSONRPCException {
        return this.doGetRequest();
    }

    public JSONArray callJSONArray(String method, JSONObject params) throws JSONRPCException {
        JSONObject response = null;

        try {
            response = this.doRequest(method, params);
            return response.getJSONArray("result");
        } catch (JSONException var7) {
            JSONException e = var7;

            try {
                if (response == null) {
                    throw new JSONRPCException("Cannot call method: " + method, e);
                } else {
                    return new JSONArray(response.getString("result"));
                }
            } catch (JSONException var6) {
                throw new JSONRPCException("Cannot convert result to JSONArray", var6);
            }
        }
    }
}
