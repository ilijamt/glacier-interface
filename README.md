Glacier Interface
=================

A command line tool to interface with glacier.

Usage
-----
glacier-interface &lt;options&gt; &lt;commands&gt; &lt;command-options&gt;

Options
-------

* **--aws-key** Sets the amazon region, if specified it will overide the data loaded from the configuration file
* **--aws-region** Sets the amazon key, if specified it will overide the data loaded from the configuration file
* **--aws-secret-key** Sets the amazon secret key, if specified it will overide the data loaded from the configuration file
* **--aws-vault** Sets the amazon vault, if specified it will overide the data loaded from the configuration file
* **--config** Location to the configuration file to load
* **--create-config** Create a config file based on the parameters you have supplied into the application
* **--directory** The base directory from which we start, if not specified then the directory is set to the current working directory

Commands
--------

* **help**
* **list-vault**
* **create-vault**
* **delete-vault**
* **list-vault-jobs**
* **vault-job-info**
* **inventory-retrieve**
* **inventory-download**
* **list-journal**
* **delete-archive**
* **upload-archive**
* **list-multipart-uploads**
* **multipart-upload-info**
* **abort-multipart-upload**