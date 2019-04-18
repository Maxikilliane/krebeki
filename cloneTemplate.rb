require 'fileutils'

def prompt(*args)
    print(*args)
    gets
end

def replaceInFiles(query, replacee)
  %x(find * -d -type f -exec perl -pi -e 's/#{query}/#{replacee}/g' {} \\;)
end

def replaceInPath(query, replacee)
  # get all files and folders in the folder
  Dir.glob("**/*")
    .select {|f| File.directory? f } # filter only directories
    .select {|p| p.end_with? query } # filter them for the query
    .each do |p|
      old_path = Dir.pwd + "/" + p + "/*"
      new_path = Dir.pwd + "/" + p.gsub(query, replacee) + "/"
      puts "Will move #{old_path} to #{new_path}"
      FileUtils.makedirs new_path
      %x(mv #{old_path} #{new_path} )
    end

    # delete all empty dirs
    %x(find . -type d -empty -delete)
end

def replaceInFileNames(query, replacee)
  %x(for hit in $(find . -iname '*#{query}*' | awk '{ print length, $0 }' | sort -n -r | cut -d" " -f2-); do
    cd $(dirname "$hit");
    rename 's/#{query}/#{replacee}/g' *;
    cd -;
  done;)
end

def commitWithMessage(message)
  puts "Commiting with Message: #{message}"
  %x(git add .)
  %x(git commit -m "#{message}")
end

# Install the rename tool. Way faster than just "brew install rename"
def installRename()
  file = File.open("Brewfile", "w")
  file.puts(%q(tap "homebrew/bundle"))
  file.puts(%q(brew "rename"))
  file.close

  %x(brew bundle)

  %x(rm Brewfile)
end
# Here starts the actual SCRIPT_OUTPUT_FILE_0

# Gather information
folder_name = (prompt "Folder name for checkout? ").strip
app_name = (prompt "Name for the new App (replaces Template)? ").strip
package_name = (prompt "New (full!) package name (replaces de.number42.template)? ").strip

%x(git clone --depth 1 git@github.com:num42/template-app-android.git)

puts "renaming to #{folder_name}"
%x(mv template-app-android #{folder_name})

Dir.chdir(folder_name) do

  puts "removing old .git history"
  %x(rm -rf .git)

  puts "init git"
  %x(git init .)

  commitWithMessage("Init")

  puts "make replacements in files"


  installRename()

  replaceInFiles("de.number42.template.TemplateApplication", "de.number42.template.#{app_name}Application")
  replaceInFileNames("TemplateApplication", "#{app_name}Application")
  replaceInFiles("TemplateApplication", "#{app_name}Application")
  replaceInFiles("template-app-android", "#{app_name}-app-android")
  replaceInFiles("Template", "#{app_name}")

  commitWithMessage("Modify App Name")

  replaceInFiles("de.number42.template", "#{package_name}")

  replaceInPath("/de/number42/template", "/#{package_name.gsub("\.", "/")}")

  commitWithMessage("Rename Package Name")
end
