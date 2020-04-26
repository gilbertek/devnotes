# Fix outdated command line tools

Warning: A newer Command Line Tools release is available.
Update them from Software Update in System Preferences or run:
  softwareupdate --all --install --force

If that doesn't show you an update run:
  rm -rf /Library/Developer/CommandLineTools
  xcode-select --install

You may need to run the command with sudo

Alternatively, manually download them from:
  https://developer.apple.com/download/more/.
