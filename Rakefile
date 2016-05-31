require 'pathname'

MAIN_PATH = File.dirname(__FILE__);

PACKAGE_FOLDER_PATH = File.join(MAIN_PATH, "react-native-revmob");
SAMPLE_APP_NODE_MODULES_PATH = File.join(MAIN_PATH, "SampleApp/node_modules");

desc "Clean"
task :clean do
	sh "rm -rf #{PACKAGE_FOLDER_PATH} && mkdir #{PACKAGE_FOLDER_PATH}"
end

desc "Copy the files that will go to the node_module"
task :copy_files_to_package_folder  do
	puts "Copying to react-native-revmob"
	sh "cp -rf index.js ios android lib package.json #{PACKAGE_FOLDER_PATH}"
end

desc "Copy react-native-revmob to SampleApp node_modules"
task :copy_package_to_sample_app_dependencies do
	puts "Copying to sample app node_modules"
	sh "cp -rf #{PACKAGE_FOLDER_PATH} #{SAMPLE_APP_NODE_MODULES_PATH}"
end

task :default do
	puts "Done"
end

task :default => [:clean, :copy_files_to_package_folder, :copy_package_to_sample_app_dependencies]
