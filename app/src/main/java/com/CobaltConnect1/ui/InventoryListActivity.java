package com.CobaltConnect1.ui;
/*

public class InventoryListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER_ID = 0;
    String TAG = "test";
    private static final NumberFormat sCurrencyFormat = DecimalFormat.getCurrencyInstance(Locale.US);

    private Account mAccount;
    private MerchantConnector mMerchantConnector;
    private InventoryConnector inventoryConnector;

    // this applies only when using content provider
    //private ItemLoader itemLoader = null;

    // this is used for all connection methods
    private boolean serviceIsBound = false;
    private IInventoryService inventoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAccount = CloverAccount.getAccount(this);
        com.CobaltConnect1(TAG, "onCreate: " +CloverAccount.KEY_MERCHANT_ID);


        if (mAccount != null) {
            mMerchantConnector = new MerchantConnector(this, mAccount, null);
            mMerchantConnector.getMerchant(new ServiceConnector.Callback<Merchant>() {
                @Override
                public void onServiceSuccess(Merchant result, ResultStatus status) {
                    (TAG, "onServiceSuccess: " +status );
                    // set the Currency
                    sCurrencyFormat.setCurrency(result.getCurrency());

                    // now start the Loader to query Inventory
                  //  getLoaderManager().initLoader(ITEM_LOADER_ID, null, InventoryListActivity.this);
                    connectToInventoryWebService();
                }

                @Override
                public void onServiceFailure(ResultStatus status) {
                    Log.e(TAG, "onServiceFailure: " +status);
                }

                @Override
                public void onServiceConnectionFailure() {
                    Log.e(TAG, "onServiceConnectionFailure: " );
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMerchantConnector.disconnect();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ITEM_LOADER_ID:
                return new CursorLoader(this, InventoryContract.Item.contentUriWithAccount(mAccount), null, null,
                        null, InventoryContract.Item.PRICE);
        }
        throw new IllegalArgumentException("Unknown loader ID");
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case ITEM_LOADER_ID:
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,
                        android.R.layout.two_line_list_item,
                        cursor,
                        new String[]{InventoryContract.Item.ID, InventoryContract.Item.PRICE},
                        new int[]{android.R.id.text1, android.R.id.text2}, 0);
                        Log.e("abhi", "onLoadFinished: " +InventoryContract.Item.NAME);

                adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        if (view.getId() == android.R.id.text2) {
                            TextView priceTextView = (TextView) view;
                            String price = "";
                            PriceType priceType = PriceType.values()[cursor.getInt(cursor.getColumnIndex(InventoryContract.Item.PRICE_TYPE))];
                            String priceString = cursor.getString(cursor.getColumnIndex(InventoryContract.Item.PRICE));
                            long value = Long.valueOf(priceString);
                            if (priceType == PriceType.FIXED) {
                                price = sCurrencyFormat.format(value / 100.0);
                            } else if (priceType == PriceType.VARIABLE) {
                                price = "Variable";
                            } else if (priceType == PriceType.PER_UNIT) {
                                price = sCurrencyFormat.format(value / 100.0) + "/" + cursor.getString(cursor.getColumnIndex(InventoryContract.Item.UNIT_NAME));
                            }

                            priceTextView.setText(price);
                            return true;
                        }
                        return false;
                    }
                });

              //  setListAdapter(adapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }




    private void connectToInventoryWebService() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InventoryItems inventoryItems = new InventoryItems();
                    inventoryItems.setName("vicky");
                    inventoryItems.setPrice("2525252525");
                    Account account = CloverAccount.getAccount(InventoryListActivity.this);
                    CloverAuth.AuthResult authResult = CloverAuth.authenticate(InventoryListActivity.this, account);
                    String baseUrl = authResult.authData.getString(CloverAccount.KEY_BASE_URL);
                    Log.e(TAG, "doInBackground: ========base url"   +baseUrl);
                    Log.e(TAG, "doInBackground: merchant id=================" +authResult.merchantId );

                    if (authResult.authToken != null && baseUrl != null) {
                        CustomHttpClient httpClient = CustomHttpClient.getHttpClient();
                       */
/* String getNameUri = "/v2/merchant/name";
                        String url = baseUrl + getNameUri + "?access_token=" + authResult.authToken;*//*

                        String getNameUri = "/v2/merchant/name";
                        String url = baseUrl + getNameUri + "?access_token=" + authResult.authToken;
                        Log.e(TAG, "doInBackground: ---url"+url);
                        String result = httpClient.get(url);
                       // String result = httpClient.post(url,"vicky");
                        JSONTokener jsonTokener = new JSONTokener(result);
                        JSONObject root = (JSONObject) jsonTokener.nextValue();
                        String merchantId = root.getString("merchantId");
                        inventoryService = new InventoryWebService(merchantId, authResult.authToken, baseUrl);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error retrieving merchant info from server", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (inventoryService != null) {
                    Log.e(TAG, "onPostExecute: -----------using web service" );
                    serviceIsBound = true;
                    fetchItemsFromService();
                } else {
                    Log.e(TAG, "onPostExecute: -----------failed to connect to web service" );

                }
            }
        }.execute();
    }




    static class InventoryWebService implements IInventoryService {
        private static final String TAG = InventoryWebService.class.getSimpleName();

        @Override
        public List<Discount> getDiscounts(ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getDiscounts()");
        }

        @Override
        public Discount getDiscount(String discountId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getDiscount()");
        }

        @Override
        public Discount createDiscount(Discount discount, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createDiscount()");
        }

        @Override
        public void updateDiscount(Discount discount, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateDiscount()");
        }

        @Override
        public void deleteDiscount(String discountId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteDiscount()");
        }

        @Override
        public void addItemToCategory(String itemId, String categoryId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement addItemToCategory()");
        }

        @Override
        public void removeItemFromCategory(String itemId, String categoryId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeItemFromCategory()");
        }

        @Override
        public void moveItemInCategoryLayout(String itemId, String categoryId, int direction, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement moveItemInCategoryLayout()");
        }

        @Override
        public void assignModifierGroupToItem(String modifierGroupId, String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement assignModifierGroupToItem()");
        }

        @Override
        public void removeModifierGroupFromItem(String modifierGroupId, String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeModifierGroupFromItem()");
        }

        @Override
        public TaxRate createTaxRate(TaxRate taxRate, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createTaxRate()");
        }

        @Override
        public void updateTaxRate(TaxRate taxRate, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateTaxRate()");
        }

        @Override
        public void deleteTaxRate(String taxRateId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteTaxRate()");
        }

        private String merchantId;
        private String accessToken;
        private String baseUrl;

        public InventoryWebService(String merchantId, String accessToken, String baseUrl) {
            this.merchantId = merchantId;
            this.accessToken = accessToken;
            this.baseUrl = baseUrl;
        }

        @Override
        public List<Item> getItems(ResultStatus resultStatus) throws RemoteException {
            String uri = "/v2/merchant/" + merchantId + "/inventory/items";
            return getArrayResults(Item.class, uri, "items", resultStatus);
        }

        @Override
        public List<Item> getItemsWithCategories(ResultStatus resultStatus) throws RemoteException {
            String uri = "/v2/merchant/" + merchantId + "/inventory/items_with_categories";
            return getArrayResults(Item.class, uri, "items", resultStatus);
        }

        @Override
        public List<String> getItemIds(ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("getItemIds() not supported through web service API");
        }

        @Override
        public Item getItem(String s, ResultStatus resultStatus) throws RemoteException {
            return null;
        }

      */
/*  @Override
        public Item getItem(String itemId, ResultStatus resultStatus) throws RemoteException {
            String uri = "/v3/merchant/" + merchantId + "/inventory/items/" + itemId;
            return getResult(Item.class, uri, "item", resultStatus);
        }*//*


        @Override
        public Item getItemWithCategories(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("getItemWithCategories() not supported through web service API");
        }

        @Override
        public Item createItem(Item item, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createItem()");
        }

        @Override
        public void updateItem(Item item, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateItem()");
        }

        @Override
        public void deleteItem(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteItem()");
        }

        @Override
        public List<Category> getCategories(ResultStatus resultStatus) throws RemoteException {
            return null;
        }

        @Override
        public List<TaxRate> getTaxRatesForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("getTaxRatesForItem() not supported through web service API");
        }

        @Override
        public void assignTaxRatesToItem(String itemId, List<String> taxRates, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement assignTaxRatesToItem()");
        }

        @Override
        public void removeTaxRatesFromItem(String itemId, List<String> taxRates, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeTaxRatesFromItem()");
        }

        @Override
        public List<TaxRate> getTaxRates(ResultStatus resultStatus) throws RemoteException {
            return null;
        }

        */
/*@Override
        public TaxRate getTaxRate(String taxRateId, ResultStatus resultStatus) throws RemoteException {
            String uri = "/v2/merchant/" + merchantId + "/tax_rates/" + taxRateId;
            return getResult(TaxRate.class, uri, "taxRate", resultStatus);
        }*//*


      */
/*  @Override
        public List<TaxRate> getTaxRates(ResultStatus resultStatus) throws RemoteException {
            String uri = "/v2/merchant/" + merchantId + "/tax_rates";
            return getArrayResults(TaxRate.class, uri, "taxRates", resultStatus);
        }
*//*

        @Override
        public TaxRate getTaxRate(String s, ResultStatus resultStatus) throws RemoteException {
            return null;
        }

       */
/* @Override
        public List<Category> getCategories(ResultStatus resultStatus) throws RemoteException {
            String uri = "/v2/merchant/" + merchantId + "/inventory/categories";
            return getArrayResults(Category.class, uri, "categories", resultStatus);
        }*//*


*/
/*        private <T> T getResult(Class<T> dataClass, String uri, String jsonFieldName, ResultStatus resultStatus) throws RemoteException {
            CustomHttpClient httpClient = CustomHttpClient.getHttpClient();

            try {
                String url = baseUrl + uri + "?access_token=" + accessToken;
                String result = httpClient.get(url);
                Log.e(TAG, "getResult: 2-----------" +result );
                if (!TextUtils.isEmpty(result)) {
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject root = (JSONObject) jsonTokener.nextValue();
                    JSONObject jsonObject = root.getJSONObject(jsonFieldName);
                    if (jsonObject != null) {
                        Constructor<T> constructor = dataClass.getConstructor(JSONObject.class);
                        return constructor.newInstance(jsonObject);
                    }
                }
            } catch (Exception e) {
                resultStatus.setStatus(ResultStatus.SERVICE_ERROR, "Error retrieving result from server");
                Log.e(TAG, "Error retrieving result from server", e);
                throw new RemoteException();
            }

            resultStatus.setStatusCode(ResultStatus.NOT_FOUND);
            return null;
        }*//*


        private <T> List<T> getArrayResults(Class<T> dataClass, String uri, String jsonFieldName, ResultStatus resultStatus) throws RemoteException {
            CustomHttpClient httpClient = CustomHttpClient.getHttpClient();

            try {
                List<T> results = new ArrayList<T>();
                String url = baseUrl + uri + "?access_token=" + accessToken;
                Log.e(TAG, "getArrayResults: ===========abc" +url );
                String result = httpClient.get(url);
                if (!TextUtils.isEmpty(result)) {
                    JSONTokener jsonTokener = new JSONTokener(result);
                    JSONObject root = (JSONObject) jsonTokener.nextValue();
                    JSONArray jsonArray = root.getJSONArray(jsonFieldName);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Constructor<T> constructor = dataClass.getConstructor(JSONObject.class);
                        T obj = constructor.newInstance(jsonObject);
                        results.add(obj);
                    }
                }
                resultStatus.setStatusCode(ResultStatus.OK);
                return results;
            } catch (Exception e) {
                resultStatus.setStatus(ResultStatus.SERVICE_ERROR, "Error retrieving result from server");
                Log.e(TAG, "Error retrieving result from server", e);
                throw new RemoteException();
            }
        }

        @Override
        public Category createCategory(Category category, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createCategory()");
        }

        @Override
        public void updateCategory(Category category, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateCategory()");
        }

        @Override
        public void deleteCategory(String categoryId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteCategory()");
        }

        @Override
        public List<ModifierGroup> getModifierGroups(ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getModifierGroups()");
        }

        @Override
        public ModifierGroup createModifierGroup(ModifierGroup group, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createModifierGroup()");
        }

        @Override
        public void updateModifierGroup(ModifierGroup group, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateModifierGroup()");
        }

        @Override
        public void deleteModifierGroup(String groupId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteModifierGroup()");
        }

        @Override
        public List<Modifier> getModifiers(String modifierGroupId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getModifiers()");
        }

        @Override
        public Modifier createModifier(String modifierGroupId, Modifier modifier, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createModifier()");
        }

        @Override
        public void updateModifier(Modifier modifier, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateModifier()");
        }

        @Override
        public void deleteModifier(String modifierId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteModifier()");
        }

        @Override
        public List<ModifierGroup> getModifierGroupsForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getModifierGroupsForItem()");
        }

        @Override
        public List<Tag> getTags(ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getTags()");
        }

        @Override
        public Tag getTag(String tagId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getTag()");
        }

        @Override
        public Tag createTag(Tag tag, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createTag()");
        }

        @Override
        public void updateTag(Tag tag, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateTag()");
        }

        @Override
        public void deleteTag(String tagId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteTag()");
        }

        @Override
        public List<Tag> getTagsForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getTagsForItem()");
        }

        @Override
        public void assignTagsToItem(String itemId, List<String> tags, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement assignTagsToItem()");
        }

        @Override
        public void removeTagsFromItem(String itemId, List<String> tags, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeTagsFromItem()");
        }

        @Override
        public void assignItemsToTag(String tagId, List<String> items, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement assignItemsToTag()");
        }

        @Override
        public void removeItemsFromTag(String tagId, List<String> items, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeItemsFromTag()");
        }

        @Override
        public List<Tag> getTagsForPrinter(String printerMac, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getTagsForPrinter()");
        }

        @Override
        public void assignTagsToPrinter(String printerMac, List<String> tags, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement assignTagsToPrinter()");
        }

        @Override
        public void removeTagsFromPrinter(String printerMac, List<String> tags, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeTagsFromPrinter()");
        }

        @Override
        public void updateModifierSortOrder(String modifierGroupId, List<String> modifierIds, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateModifierSortOrder()");
        }

        @Override
        public List<Attribute> getAttributes(ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getAttributes()");
        }

        @Override
        public Attribute getAttribute(String attributeId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getAttribute()");
        }

        @Override
        public Attribute createAttribute(Attribute attribute, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createAttribute()");
        }

        @Override
        public void updateAttribute(Attribute attribute, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateAttribute()");
        }

        @Override
        public void deleteAttribute(String attributeId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteAttribute()");
        }

        @Override
        public List<Option> getOptions(ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getOptions()");
        }

        @Override
        public Option getOption(String optionId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getOption()");
        }

        @Override
        public Option createOption(Option option, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createOption()");
        }

        @Override
        public void updateOption(Option option, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateOption()");
        }

        @Override
        public void deleteOption(String optionId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteOption()");
        }

        @Override
        public List<Option> getOptionsForItem(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getOptionsForItem()");
        }

        @Override
        public void assignOptionsToItem(String itemId, List<String> optionIds, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement assignOptionsToItem()");
        }

        @Override
        public void removeOptionsFromItem(String itemId, List<String> optionIds, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeOptionsFromItem()");
        }

        @Override
        public void updateItemStock(String itemId, long stockCount, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateItemStock()");
        }

        @Override
        public void removeItemStock(String itemId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement removeItemStock()");
        }

        @Override
        public ItemGroup getItemGroup(String itemGroupId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement getItemGroup()");
        }

        @Override
        public ItemGroup createItemGroup(ItemGroup itemGroup, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement createItemGroup()");
        }

        @Override
        public void updateItemGroup(ItemGroup itemGroup, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateItemGroup()");
        }

        @Override
        public void deleteItemGroup(String itemGroupId, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement deleteItemGroup()");
        }

        @Override
        public void updateItemStockQuantity(String itemId, double quantity, ResultStatus resultStatus) throws RemoteException {
            throw new UnsupportedOperationException("Need to implement updateItemStockQuantity()");
        }

        @Override
        public IBinder asBinder() {
            throw new UnsupportedOperationException("Not a real Android service");
        }
    }


    // This should work for both bound AIDL service and web service
    private void fetchItemsFromService() {
        if (serviceIsBound && inventoryService != null) {
            new AsyncTask<Void, Void, Item>() {
                @Override
                protected Item doInBackground(Void... params) {
                    Item item = null;
                    try {
                        ResultStatus getItemsStatus = new ResultStatus();
                        List<Item> items = inventoryService.getItems(getItemsStatus);
                        Log.i(TAG, "Received result from getItems(): " + getItemsStatus);
                        if (getItemsStatus.isSuccess()) {
                            for (Item i : items) {
                                Log.e(TAG, "item = ====" + dumpItem(i));
                            }
                        }
                        if (items != null && items.size() > 0) {
                            String itemId = items.get(0).getId();
                            ResultStatus getItemStatus = new ResultStatus();
                            item = inventoryService.getItem(itemId, getItemStatus);
                            Log.i(TAG, "Received result from getItem(): " + getItemStatus);
                        }

                        ResultStatus resultStatus = new ResultStatus();
                        List<Category> categories = inventoryService.getCategories(resultStatus);
                        if (resultStatus.isSuccess()) {
                            for (Category category : categories) {
                                Log.i(TAG, "category = " + dumpCategory(category));
                            }
                        } else {
                            Log.i(TAG, "Couldn't retrieve categories: " + resultStatus);
                        }
                        List<TaxRate> taxRates = inventoryService.getTaxRates(resultStatus);
                        if (resultStatus.isSuccess()) {
                            for (TaxRate taxRate : taxRates) {
                                Log.i(TAG, "tax rate = " + dumpTaxRate(taxRate));
                            }
                        } else {
                            Log.i(TAG, "Couldn't retrieve tax rates: " + resultStatus);
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "Error calling inventory service", e);
                    }
                    return item;
                }

                @Override
                protected void onPostExecute(Item result) {
                    displayItem(result);
                }
            }.execute();
        }
    }


    private void displayItem(Item item) {
        */
/*if (item != null) {
            String textViewContents = "";
            CharSequence text = resultText.getText();
            if (text != null) {
                textViewContents = text.toString();
            }
            resultText.setText(textViewContents + "\nitem = " + dumpItem(item));
        }*//*

    }

    private String dumpItem(Item item) {
        return item != null ? String.format("%s{id=%s, name=%s, price=%d}", Item.class.getSimpleName(), item.getId(), item.getName(), item.getPrice()) : null;
    }

    private String dumpTaxRate(TaxRate taxRate) {
        return String.format("%s{id=%s, name=%s, rate=%d, is default=%b}", TaxRate.class.getSimpleName(), taxRate.getId(), taxRate.getName(), taxRate.getRate(), taxRate.getIsDefault());
    }

    private String dumpModifier(Modifier modifier) {
        return String.format("%s{id=%s, name=%s}", Modifier.class.getSimpleName(), modifier.getId(), modifier.getName());
    }

    private String dumpModifierGroup(ModifierGroup modifierGroup) {
        return String.format("%s{id=%s, name=%s}", ModifierGroup.class.getSimpleName(), modifierGroup.getId(), modifierGroup.getName());
    }

    private String dumpCategory(Category category) {
        return String.format("%s{id=%s, name=%s, sort order=%d}", Category.class.getSimpleName(), category.getId(), category.getName(), category.getSortOrder());
    }


    static class CustomHttpClient extends DefaultHttpClient {
        private static final int CONNECT_TIMEOUT = 60000;
        private static final int READ_TIMEOUT = 60000;
        private static final int MAX_TOTAL_CONNECTIONS = 5;
        private static final int MAX_CONNECTIONS_PER_ROUTE = 3;
        private static final int SOCKET_BUFFER_SIZE = 8192;
        private static final boolean FOLLOW_REDIRECT = false;
        private static final boolean STALE_CHECKING_ENABLED = true;
        private static final String CHARSET = HTTP.toString();
        private static final HttpVersion HTTP_VERSION = HttpVersion.HTTP_1_1;
        private static final String USER_AGENT = "CustomHttpClient"; // + version

        public static CustomHttpClient getHttpClient() {
            CustomHttpClient httpClient = new CustomHttpClient();
            final HttpParams params = httpClient.getParams();
            HttpProtocolParams.setUserAgent(params, USER_AGENT);
            HttpProtocolParams.setContentCharset(params, CHARSET);
            HttpProtocolParams.setVersion(params, HTTP_VERSION);
            HttpClientParams.setRedirecting(params, FOLLOW_REDIRECT);
            HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
            HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
            HttpConnectionParams.setStaleCheckingEnabled(params, STALE_CHECKING_ENABLED);
            ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
            ConnManagerParams.setMaxTotalConnections(params, MAX_TOTAL_CONNECTIONS);
            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));

            return httpClient;
        }

        public String get(String url) throws IOException, HttpException {
            String result;
            HttpGet request = new HttpGet(url);
            HttpResponse response = execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            Log.e("abhi", "get: status code------------" +statusCode );
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                } else {
                    throw new HttpException("Received empty body from HTTP response");
                }
            } else {
                throw new HttpException("Received non-OK status from server: " + response.getStatusLine());
            }
            return result;
        }

        @SuppressWarnings("unused")
        public String post(String url, String body) throws IOException, HttpException {
            String result;
            HttpPost request = new HttpPost(url);
            HttpEntity bodyEntity = new StringEntity(body);
            request.setEntity(bodyEntity);
            HttpResponse response = execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                } else {
                    throw new HttpException("Received empty body from HTTP response");
                }
            } else {
                throw new HttpException("Received non-OK status from server: " + response.getStatusLine());
            }
            return result;
        }

    }
}

*/
